package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

sealed class PdfLoadState {
    object Idle : PdfLoadState()
    data class Loading(val progress: Float = 0f, val message: String = "Connecting...") : PdfLoadState()
    data class Success(val file: File, val pageCount: Int) : PdfLoadState()
    data class Error(val message: String) : PdfLoadState()
}

class PdfManager(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    fun getLocalPdfFile(bookId: String): File {
        val pdfDir = File(context.filesDir, "pdfs")
        if (!pdfDir.exists()) {
            pdfDir.mkdirs()
        }
        return File(pdfDir, "$bookId.pdf")
    }

    fun isBookDownloaded(bookId: String): Boolean {
        val localFile = getLocalPdfFile(bookId)
        return localFile.exists() && localFile.length() > 1024
    }

    suspend fun downloadOrGetPdf(
        bookId: String,
        pdfUrl: String,
        onProgress: (Float) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        val localFile = getLocalPdfFile(bookId)

        // If file already exists and has non-zero size (> 1KB), return local file directly
        if (localFile.exists() && localFile.length() > 1024) {
            return@withContext Result.success(localFile)
        }

        if (pdfUrl.isBlank()) {
            return@withContext Result.failure(Exception("PDF URL is missing or empty"))
        }

        try {
            val request = Request.Builder()
                .url(pdfUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:109.0) Gecko/109.0 Firefox/115.0")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Failed to download PDF. Server returned HTTP ${response.code}"))
            }

            val body = response.body ?: return@withContext Result.failure(Exception("Empty response received from PDF host"))
            val contentLength = body.contentLength()

            val tempFile = File(context.cacheDir, "temp_$bookId.pdf")
            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(tempFile)

            val buffer = ByteArray(8192)
            var bytesRead: Int
            var totalBytesRead = 0L

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                if (contentLength > 0) {
                    val progress = (totalBytesRead.toFloat() / contentLength.toFloat()).coerceIn(0f, 1f)
                    onProgress(progress)
                }
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            if (tempFile.length() <= 1024) {
                tempFile.delete()
                return@withContext Result.failure(Exception("Downloaded PDF file is corrupted or empty"))
            }

            // Copy temp file to permanent local storage
            tempFile.copyTo(localFile, overwrite = true)
            tempFile.delete()

            Result.success(localFile)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception(e.localizedMessage ?: "Network error downloading PDF file"))
        }
    }

    suspend fun getPageCount(file: File): Int = withContext(Dispatchers.IO) {
        try {
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)
            val count = renderer.pageCount
            renderer.close()
            pfd.close()
            count
        } catch (e: Exception) {
            0
        }
    }

    suspend fun renderPageBitmap(
        file: File,
        pageIndex: Int,
        targetWidth: Int = 1080
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)
            if (pageIndex < 0 || pageIndex >= renderer.pageCount) {
                renderer.close()
                pfd.close()
                return@withContext null
            }
            val page = renderer.openPage(pageIndex)
            val aspectRatio = page.height.toFloat() / page.width.toFloat()
            val calculatedWidth = targetWidth
            val calculatedHeight = (targetWidth * aspectRatio).toInt().coerceAtLeast(100)

            val bitmap = Bitmap.createBitmap(calculatedWidth, calculatedHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            renderer.close()
            pfd.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
