package com.example.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import com.example.data.model.BookEntity
import com.example.data.repository.InitialDataSeed
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class PdfManager(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .build()

    // Fast in-memory thumbnail memory cache for ultra-smooth 60fps scrolling
    private val thumbnailMemoryCache = object : android.util.LruCache<String, Bitmap>(128) {}

    init {
        // Clean up legacy artificial generated covers so that ONLY authentic PDF first pages are used
        try {
            val legacyThumbnailDir = File(context.filesDir, "book_covers")
            if (legacyThumbnailDir.exists()) {
                legacyThumbnailDir.listFiles()?.forEach { file ->
                    if (file.name.endsWith(".png")) {
                        file.delete()
                    }
                }
            }
        } catch (_: Exception) {}
    }

    fun getLocalPdfFile(bookId: String): File {
        val pdfDir = File(context.filesDir, "pdfs")
        if (!pdfDir.exists()) {
            pdfDir.mkdirs()
        }
        val sanitizedId = bookId.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        return File(pdfDir, "$sanitizedId.pdf")
    }

    fun isBookDownloaded(bookId: String): Boolean {
        val localFile = getLocalPdfFile(bookId)
        return localFile.exists() && localFile.length() > 2048
    }

    /**
     * Copies the downloaded PDF into the device's public Downloads directory
     * so the user can find it in mobile storage / Downloads / My Files.
     */
    fun copyPdfToDeviceDownloads(bookId: String, bookTitle: String): Uri? {
        return try {
            val srcFile = getLocalPdfFile(bookId)
            if (!srcFile.exists() || srcFile.length() <= 2048) return null

            val sanitizedTitle = bookTitle.trim().replace("[\\\\/:*?\"<>|]".toRegex(), "_")
            val fileName = if (sanitizedTitle.isNotBlank()) "$sanitizedTitle.pdf" else "Book_$bookId.pdf"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Baytul Ilm")
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { out ->
                        srcFile.inputStream().use { inp ->
                            inp.copyTo(out)
                        }
                    }
                    Log.d("PdfManager", "Saved PDF to mobile Downloads/Baytul Ilm/$fileName")
                    uri
                } else null
            } else {
                val downloadsDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Baytul Ilm")
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val destFile = File(downloadsDir, fileName)
                srcFile.copyTo(destFile, overwrite = true)
                MediaScannerConnection.scanFile(context, arrayOf(destFile.absolutePath), arrayOf("application/pdf"), null)
                Log.d("PdfManager", "Saved PDF to mobile Downloads: ${destFile.absolutePath}")
                Uri.fromFile(destFile)
            }
        } catch (e: Exception) {
            Log.w("PdfManager", "Failed to copy PDF to public Downloads: ${e.message}")
            null
        }
    }

    /**
     * Downloads or returns the local cached PDF file.
     * @param onProgress Callback receiving (bytesRead: Long, totalBytes: Long, progressFraction: Float)
     */
    suspend fun downloadOrGetPdf(
        bookId: String,
        pdfUrl: String,
        onProgress: (bytesRead: Long, totalBytes: Long, progressFraction: Float) -> Unit = { _, _, _ -> }
    ): Result<File> = withContext(Dispatchers.IO) {
        val localFile = getLocalPdfFile(bookId)

        // 1. If file already exists locally and is valid (>2KB), return it immediately (0s delay!)
        if (localFile.exists() && localFile.length() > 2048) {
            return@withContext Result.success(localFile)
        }

        val trimmedUrl = pdfUrl.trim()
        if (trimmedUrl.isBlank()) {
            return@withContext Result.failure(Exception("PDF کا لنک موجود نہیں ہے (URL is empty)"))
        }

        // Clean & validate URL
        val validUrl = try {
            val uri = Uri.parse(trimmedUrl)
            if (uri.scheme == null) "https://$trimmedUrl" else trimmedUrl
        } catch (e: Exception) {
            trimmedUrl
        }

        // Retry mechanism: Attempt up to 3 times for transient network drops
        var lastException: Exception? = null
        for (attempt in 1..3) {
            var tempFile: File? = null
            try {
                val httpUrl = validUrl.toHttpUrlOrNull()
                val requestBuilder = if (httpUrl != null) {
                    Request.Builder().url(httpUrl)
                } else {
                    Request.Builder().url(validUrl)
                }
                requestBuilder
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                    .header("Accept", "application/pdf,application/octet-stream,*/*")
                    .header("Accept-Encoding", "identity")

                val response = client.newCall(requestBuilder.build()).execute()
                if (!response.isSuccessful) {
                    val code = response.code
                    response.close()
                    throw Exception("سرور کا جواب نامناسب رہا (HTTP $code)")
                }

                val body = response.body ?: throw Exception("سرور سے ڈیٹا موصول نہیں ہوا")
                val contentLength = body.contentLength()

                val temp = File(context.cacheDir, "temp_${System.currentTimeMillis()}_${localFile.name}")
                tempFile = temp
                val inputStream = body.byteStream()
                val outputStream = FileOutputStream(temp)

                val buffer = ByteArray(65536) // 64KB buffer for better performance
                var bytesRead: Int
                var totalBytesRead = 0L

                try {
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        coroutineContext.ensureActive()
                        outputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        val fraction = if (contentLength > 0) {
                            (totalBytesRead.toFloat() / contentLength.toFloat()).coerceIn(0f, 1f)
                        } else {
                            -1f
                        }
                        onProgress(totalBytesRead, contentLength, fraction)
                    }

                    outputStream.flush()
                } finally {
                    try { outputStream.close() } catch (_: Exception) {}
                    try { inputStream.close() } catch (_: Exception) {}
                    try { response.close() } catch (_: Exception) {}
                }

                if (temp.length() <= 2048) {
                    temp.delete()
                    throw Exception("ڈاؤنلوڈ شدہ فائل نامکمل ہے")
                }

                // Verify valid PDF header (%PDF-)
                val isValidPdf = try {
                    temp.inputStream().use { stream ->
                        val header = ByteArray(5)
                        val read = stream.read(header)
                        read == 5 && String(header).startsWith("%PDF")
                    }
                } catch (e: Exception) {
                    true // Continue if check fails
                }

                if (!isValidPdf && temp.length() < 10000) {
                    temp.delete()
                    throw Exception("فائل درست PDF فارمیٹ میں نہیں ہے")
                }

                // Move temp file to persistent local storage
                temp.copyTo(localFile, overwrite = true)
                temp.delete()

                return@withContext Result.success(localFile)
            } catch (e: CancellationException) {
                try { tempFile?.delete() } catch (_: Exception) {}
                throw e
            } catch (e: Exception) {
                try { tempFile?.delete() } catch (_: Exception) {}
                lastException = e
                Log.w("PdfManager", "Attempt $attempt failed: ${e.message}")
                if (attempt < 3) {
                    delay(2000)
                }
            }
        }

        Result.failure(lastException ?: Exception("انٹرنیٹ کنکشن کا مسئلہ یا ٹائم آؤٹ ہو گیا ہے"))
    }

    suspend fun getPageCount(file: File): Int = withContext(Dispatchers.IO) {
        try {
            if (!file.exists() || file.length() <= 2048) return@withContext 0
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)
            val count = renderer.pageCount
            renderer.close()
            pfd.close()
            count
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private val pageBitmapCache = object : android.util.LruCache<String, Bitmap>(32) {}
    private val renderMutex = Mutex()

    suspend fun renderPageBitmap(
        file: File,
        pageIndex: Int,
        targetWidth: Int = 1080
    ): Bitmap? = withContext(Dispatchers.IO) {
        if (!file.exists() || file.length() <= 2048) return@withContext null
        val cacheKey = "${file.absolutePath}_${pageIndex}_$targetWidth"
        pageBitmapCache.get(cacheKey)?.let { cached ->
            if (!cached.isRecycled) return@withContext cached
        }

        renderMutex.withLock {
            // Re-check cache after acquiring lock
            pageBitmapCache.get(cacheKey)?.let { cached ->
                if (!cached.isRecycled) return@withLock cached
            }

            try {
                val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(pfd)
                if (pageIndex < 0 || pageIndex >= renderer.pageCount) {
                    renderer.close()
                    pfd.close()
                    return@withLock null
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
                pageBitmapCache.put(cacheKey, bitmap)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun getDownloadedCoverFile(bookId: String): File {
        val coversDir = File(context.filesDir, "downloaded_covers")
        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }
        val sanitizedId = bookId.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        return File(coversDir, "${sanitizedId}_first_page.jpg")
    }

    fun invalidateCoverCache(bookId: String) {
        val cacheKey = "cover_$bookId"
        val downloadedCacheKey = "downloaded_first_page_$bookId"
        thumbnailMemoryCache.remove(cacheKey)
        thumbnailMemoryCache.remove(downloadedCacheKey)
        pageBitmapCache.remove(cacheKey)
    }

    suspend fun extractAndSaveFirstPageCover(bookId: String, pdfFile: File): Bitmap? = withContext(Dispatchers.IO) {
        if (!pdfFile.exists() || pdfFile.length() <= 2048) return@withContext null

        try {
            // Render page 0 (the first page) as high-clarity cover
            val bitmap = renderPageBitmap(pdfFile, 0, 720)
            if (bitmap != null) {
                // 1. Save to dedicated downloaded cover storage
                val downloadedCoverFile = getDownloadedCoverFile(bookId)
                try {
                    downloadedCoverFile.outputStream().use {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 92, it)
                    }
                } catch (e: Exception) {
                    Log.e("PdfManager", "Error saving downloaded cover file for $bookId", e)
                }

                // 2. Also overwrite generic thumbnail file
                val thumbnailFile = getThumbnailFile(bookId)
                try {
                    thumbnailFile.outputStream().use {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 92, it)
                    }
                } catch (_: Exception) {}

                // 3. Update fast in-memory caches
                thumbnailMemoryCache.put("downloaded_first_page_$bookId", bitmap)
                thumbnailMemoryCache.put("cover_$bookId", bitmap)
                pageBitmapCache.put("cover_$bookId", bitmap)
                return@withContext bitmap
            }
        } catch (e: Exception) {
            Log.e("PdfManager", "Error rendering first page cover for book $bookId", e)
        }
        null
    }

    suspend fun getOrRenderThumbnail(bookId: String, pdfFile: File): Bitmap? = withContext(Dispatchers.IO) {
        extractAndSaveFirstPageCover(bookId, pdfFile)
    }

    /**
     * Extracts and caches the cover of any book.
     * If the book is downloaded, its first page is STRICTLY used as the cover.
     */
    suspend fun getOrExtractBookCover(
        bookId: String,
        pdfUrl: String? = null,
        coverUrl: String? = null,
        book: BookEntity? = null
    ): Bitmap? = withContext(Dispatchers.IO) {
        val localPdf = getLocalPdfFile(bookId)
        val hasLocalPdf = localPdf.exists() && localPdf.length() > 2048
        val hasLocalPathPdf = !pdfUrl.isNullOrBlank() && File(pdfUrl).exists() && File(pdfUrl).length() > 2048
        val isExplicitlyDownloaded = book?.isDownloaded == true || isBookDownloaded(bookId) || hasLocalPdf || hasLocalPathPdf

        // 1. IF DOWNLOADED: The cover MUST strictly be the first page of the PDF!
        if (isExplicitlyDownloaded) {
            val targetPdf = if (hasLocalPdf) localPdf else if (hasLocalPathPdf) File(pdfUrl!!) else localPdf
            val downloadedCacheKey = "downloaded_first_page_$bookId"

            // Check in-memory cache first
            thumbnailMemoryCache.get(downloadedCacheKey)?.let { cached ->
                if (!cached.isRecycled) return@withContext cached
            }

            // Check persistent downloaded cover storage
            val downloadedCoverFile = getDownloadedCoverFile(bookId)
            if (downloadedCoverFile.exists() && downloadedCoverFile.length() > 500) {
                try {
                    val bitmap = android.graphics.BitmapFactory.decodeFile(downloadedCoverFile.absolutePath)
                    if (bitmap != null && bitmap.width > 20 && bitmap.height > 20) {
                        thumbnailMemoryCache.put(downloadedCacheKey, bitmap)
                        thumbnailMemoryCache.put("cover_$bookId", bitmap)
                        return@withContext bitmap
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // If not rendered on disk yet, extract first page directly from the downloaded PDF
            if (targetPdf.exists() && targetPdf.length() > 2048) {
                val firstPageCover = extractAndSaveFirstPageCover(bookId, targetPdf)
                if (firstPageCover != null) {
                    return@withContext firstPageCover
                }
            }
        }

        // 2. IF NOT DOWNLOADED: Check memory cache and pre-download thumbnail files
        val cacheKey = "cover_$bookId"
        thumbnailMemoryCache.get(cacheKey)?.let { cached ->
            if (!cached.isRecycled) return@withContext cached
        }
        pageBitmapCache.get(cacheKey)?.let { cached ->
            if (!cached.isRecycled) {
                thumbnailMemoryCache.put(cacheKey, cached)
                return@withContext cached
            }
        }

        val thumbnailFile = getThumbnailFile(bookId)

        if (thumbnailFile.exists() && thumbnailFile.length() > 500) {
            try {
                val bitmap = android.graphics.BitmapFactory.decodeFile(thumbnailFile.absolutePath)
                if (bitmap != null && bitmap.width > 20 && bitmap.height > 20) {
                    thumbnailMemoryCache.put(cacheKey, bitmap)
                    pageBitmapCache.put(cacheKey, bitmap)
                    return@withContext bitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 3. If coverUrl is a local file path
        val cleanCoverUrl = coverUrl?.trim().orEmpty()
        if (cleanCoverUrl.startsWith("/") || cleanCoverUrl.startsWith("file://")) {
            val localPath = cleanCoverUrl.removePrefix("file://")
            val localCoverFile = File(localPath)
            if (localCoverFile.exists() && localCoverFile.length() > 500) {
                try {
                    val bitmap = android.graphics.BitmapFactory.decodeFile(localCoverFile.absolutePath)
                    if (bitmap != null) {
                        try {
                            thumbnailFile.outputStream().use {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                            }
                        } catch (_: Exception) {}
                        thumbnailMemoryCache.put(cacheKey, bitmap)
                        pageBitmapCache.put(cacheKey, bitmap)
                        return@withContext bitmap
                    }
                } catch (_: Exception) {}
            }
        }

        // 4. If coverUrl is a web URL, download and cache it
        if (cleanCoverUrl.startsWith("http://", ignoreCase = true) || cleanCoverUrl.startsWith("https://", ignoreCase = true)) {
            try {
                val req = Request.Builder()
                    .url(cleanCoverUrl)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .build()
                val resp = client.newCall(req).execute()
                if (resp.isSuccessful) {
                    val bytes = resp.body?.bytes()
                    if (bytes != null && bytes.size > 1024) {
                        val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        if (bitmap != null && bitmap.width > 20 && bitmap.height > 20) {
                            try {
                                thumbnailFile.outputStream().use {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                                }
                            } catch (_: Exception) {}
                            thumbnailMemoryCache.put(cacheKey, bitmap)
                            pageBitmapCache.put(cacheKey, bitmap)
                            return@withContext bitmap
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("PdfManager", "Direct cover fetch failed: ${e.message}")
            }
        }

        // 4b. Try Archive.org real item cover if PDF is hosted on Archive.org
        val cleanPdfUrl = pdfUrl?.trim().orEmpty()
        if (cleanPdfUrl.contains("archive.org", ignoreCase = true)) {
            try {
                val archiveItemId = when {
                    cleanPdfUrl.contains("archive.org/download/") -> cleanPdfUrl.substringAfter("archive.org/download/").substringBefore("/")
                    cleanPdfUrl.contains("/items/") -> cleanPdfUrl.substringAfter("/items/").substringBefore("/")
                    cleanPdfUrl.contains("archive.org/details/") -> cleanPdfUrl.substringAfter("archive.org/details/").substringBefore("/")
                    else -> ""
                }
                if (archiveItemId.isNotBlank()) {
                    val coverPreviewUrls = listOf(
                        "https://archive.org/services/img/$archiveItemId",
                        "https://archive.org/download/$archiveItemId/page/cover_t.jpg"
                    )
                    for (coverUrlTry in coverPreviewUrls) {
                        try {
                            val req = Request.Builder()
                                .url(coverUrlTry)
                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                                .build()
                            val resp = client.newCall(req).execute()
                            if (resp.isSuccessful) {
                                val bytes = resp.body?.bytes()
                                if (bytes != null && bytes.size > 1024) {
                                    val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    if (bitmap != null && bitmap.width > 30 && bitmap.height > 30) {
                                        try {
                                            thumbnailFile.outputStream().use {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                                            }
                                        } catch (_: Exception) {}
                                        thumbnailMemoryCache.put(cacheKey, bitmap)
                                        pageBitmapCache.put(cacheKey, bitmap)
                                        return@withContext bitmap
                                    }
                                }
                            }
                        } catch (_: Exception) {}
                    }
                }
            } catch (e: Exception) {
                Log.d("PdfManager", "Archive.org cover fetch failed: ${e.message}")
            }
        }

        // Return null if not downloaded and no real online cover found - never generate fake covers
        null
    }

    fun getThumbnailFile(bookId: String): File {
        val thumbnailDir = File(context.filesDir, "book_covers")
        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdirs()
        }
        val sanitizedId = bookId.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        return File(thumbnailDir, "$sanitizedId.jpg")
    }
}
