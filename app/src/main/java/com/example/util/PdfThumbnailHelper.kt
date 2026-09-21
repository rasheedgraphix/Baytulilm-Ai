package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

object PdfThumbnailHelper {

    suspend fun getFirstPage(context: Context, pdfUrl: String, bookId: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            if (pdfUrl.isBlank()) return@withContext null
            val cleanId = bookId.replace(Regex("[^A-Za-z0-9]"), "_")
            val file = File(context.cacheDir, "$cleanId.pdf")

            // Agar file nahi hai to download karo
            if (!file.exists()) {
                URL(pdfUrl).openStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }

            val fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fd)
            if (renderer.pageCount == 0) {
                renderer.close()
                fd.close()
                return@withContext null
            }
            val page = renderer.openPage(0)
            // HD thumbnail ke liye scale
            val scale = 2
            val bitmap = Bitmap.createBitmap(page.width * scale, page.height * scale, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            renderer.close()
            fd.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
