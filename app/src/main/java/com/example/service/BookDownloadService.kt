package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.local.AppDatabase
import com.example.ui.viewmodel.BookDownloadProgress
import com.example.util.BookDownloadManager
import com.example.util.PdfManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class BookDownloadService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeJobs = ConcurrentHashMap<String, Job>()
    private val activeBookTitles = ConcurrentHashMap<String, String>()

    private lateinit var notificationManager: NotificationManager
    private lateinit var pdfManager: PdfManager
    private lateinit var database: AppDatabase

    companion object {
        private const val TAG = "BookDownloadService"

        const val ACTION_START_DOWNLOAD = "com.example.service.action.START_DOWNLOAD"
        const val ACTION_CANCEL_DOWNLOAD = "com.example.service.action.CANCEL_DOWNLOAD"

        const val EXTRA_BOOK_ID = "extra_book_id"
        const val EXTRA_BOOK_TITLE = "extra_book_title"
        const val EXTRA_PDF_URL = "extra_pdf_url"

        const val CHANNEL_ID = "baytul_ilm_book_downloads"
        const val FOREGROUND_NOTIFICATION_ID = 9001
        private const val NOTIFICATION_COMPLETED_BASE = 10000
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        pdfManager = PdfManager(applicationContext)
        database = AppDatabase.getDatabase(applicationContext)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val bookId = intent?.getStringExtra(EXTRA_BOOK_ID)

        if (bookId == null) {
            if (activeJobs.isEmpty()) {
                stopForegroundAndSelf()
            }
            return START_NOT_STICKY
        }

        when (action) {
            ACTION_START_DOWNLOAD -> {
                val bookTitle = intent.getStringExtra(EXTRA_BOOK_TITLE) ?: "کتاب"
                val pdfUrl = intent.getStringExtra(EXTRA_PDF_URL) ?: ""
                startBookDownload(bookId, bookTitle, pdfUrl)
            }
            ACTION_CANCEL_DOWNLOAD -> {
                cancelBookDownload(bookId)
            }
        }

        return START_NOT_STICKY
    }

    private fun startBookDownload(bookId: String, bookTitle: String, pdfUrl: String) {
        // If already downloading, don't restart
        if (activeJobs[bookId]?.isActive == true) {
            return
        }

        activeBookTitles[bookId] = bookTitle

        // Ensure service is in foreground with initial notification
        val initialNotification = buildProgressNotification(
            bookTitle = bookTitle,
            bookId = bookId,
            progressFraction = 0f,
            bytesRead = 0L,
            totalBytes = 0L
        )
        startServiceInForeground(initialNotification)

        val job = serviceScope.launch {
            var lastNotificationUpdateTime = 0L

            try {
                // Update BookDownloadManager
                BookDownloadManager.updateProgress(
                    BookDownloadProgress(
                        bookId = bookId,
                        isDownloading = true,
                        progress = 0f,
                        isCancelled = false,
                        isCompleted = false
                    )
                )

                val result = pdfManager.downloadOrGetPdf(
                    bookId = bookId,
                    pdfUrl = pdfUrl,
                    onProgress = { bytesRead, totalBytes, fraction ->
                        val safeProg = if (fraction >= 0f) fraction else 0f

                        // Update shared StateFlow for Compose UI
                        BookDownloadManager.updateProgress(
                            BookDownloadProgress(
                                bookId = bookId,
                                isDownloading = true,
                                progress = safeProg,
                                bytesRead = bytesRead,
                                totalBytes = totalBytes,
                                isCancelled = false,
                                isCompleted = false
                            )
                        )

                        // Throttle notification updates (~300ms) for smooth notification tray performance
                        val now = System.currentTimeMillis()
                        if (now - lastNotificationUpdateTime >= 300 || safeProg >= 1f) {
                            lastNotificationUpdateTime = now
                            val updatedNotification = buildProgressNotification(
                                bookTitle = bookTitle,
                                bookId = bookId,
                                progressFraction = safeProg,
                                bytesRead = bytesRead,
                                totalBytes = totalBytes
                            )
                            notificationManager.notify(FOREGROUND_NOTIFICATION_ID, updatedNotification)
                        }
                    }
                )

                if (result.isSuccess) {
                    val downloadedFile = result.getOrNull()
                    if (downloadedFile != null && downloadedFile.exists()) {
                        pdfManager.invalidateCoverCache(bookId)
                        pdfManager.extractAndSaveFirstPageCover(bookId, downloadedFile)
                        pdfManager.copyPdfToDeviceDownloads(bookId, bookTitle)
                    }

                    // Update Room Database
                    database.bookDao().updateDownloadStatus(bookId, isDownloaded = true, progress = 1.0f)

                    // Update Manager
                    BookDownloadManager.updateProgress(
                        BookDownloadProgress(
                            bookId = bookId,
                            isDownloading = false,
                            progress = 1.0f,
                            isCancelled = false,
                            isCompleted = true
                        )
                    )

                    // Show Completed Notification
                    showCompletedNotification(bookId, bookTitle)
                } else {
                    database.bookDao().updateDownloadStatus(bookId, isDownloaded = false, progress = 0f)
                    val errorMsg = result.exceptionOrNull()?.message ?: "ڈاؤن لوڈ مکمل نہیں ہو سکا"
                    BookDownloadManager.updateProgress(
                        BookDownloadProgress(
                            bookId = bookId,
                            isDownloading = false,
                            progress = 0f,
                            isCancelled = false,
                            isCompleted = false,
                            errorMessage = errorMsg
                        )
                    )
                    showFailedNotification(bookId, bookTitle, errorMsg)
                }
            } catch (e: CancellationException) {
                database.bookDao().updateDownloadStatus(bookId, isDownloaded = false, progress = 0f)
                val localFile = pdfManager.getLocalPdfFile(bookId)
                if (localFile.exists() && localFile.length() < 10000) {
                    localFile.delete()
                }
                BookDownloadManager.updateProgress(
                    BookDownloadProgress(
                        bookId = bookId,
                        isDownloading = false,
                        progress = 0f,
                        isCancelled = true,
                        isCompleted = false,
                        errorMessage = "ڈاؤن لوڈ منسوخ کر دیا گیا"
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading book $bookId", e)
                database.bookDao().updateDownloadStatus(bookId, isDownloaded = false, progress = 0f)
                val errorMsg = e.message ?: "ڈاؤن لوڈ میں مسئلہ پیش آیا"
                BookDownloadManager.updateProgress(
                    BookDownloadProgress(
                        bookId = bookId,
                        isDownloading = false,
                        progress = 0f,
                        isCancelled = false,
                        isCompleted = false,
                        errorMessage = errorMsg
                    )
                )
                showFailedNotification(bookId, bookTitle, errorMsg)
            } finally {
                activeJobs.remove(bookId)
                activeBookTitles.remove(bookId)

                if (activeJobs.isEmpty()) {
                    stopForegroundAndSelf()
                } else {
                    // Update foreground notification to next active book
                    val nextBook = activeJobs.keys.firstOrNull()
                    if (nextBook != null) {
                        val nextTitle = activeBookTitles[nextBook] ?: "کتاب"
                        val nextNotification = buildProgressNotification(
                            bookTitle = nextTitle,
                            bookId = nextBook,
                            progressFraction = 0f,
                            bytesRead = 0L,
                            totalBytes = 0L
                        )
                        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, nextNotification)
                    }
                }
            }
        }

        activeJobs[bookId] = job
    }

    private fun cancelBookDownload(bookId: String) {
        val job = activeJobs.remove(bookId)
        job?.cancel()
        activeBookTitles.remove(bookId)

        serviceScope.launch {
            try {
                database.bookDao().updateDownloadStatus(bookId, isDownloaded = false, progress = 0f)
                val localFile = pdfManager.getLocalPdfFile(bookId)
                if (localFile.exists()) {
                    localFile.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        BookDownloadManager.updateProgress(
            BookDownloadProgress(
                bookId = bookId,
                isDownloading = false,
                isCancelled = true,
                progress = 0f,
                errorMessage = "ڈاؤن لوڈ منسوخ کر دیا گیا"
            )
        )

        if (activeJobs.isEmpty()) {
            stopForegroundAndSelf()
        }
    }

    private fun startServiceInForeground(notification: Notification) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    FOREGROUND_NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                startForeground(FOREGROUND_NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
            try {
                startForeground(FOREGROUND_NOTIFICATION_ID, notification)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun stopForegroundAndSelf() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_REMOVE)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopSelf()
    }

    private fun buildProgressNotification(
        bookTitle: String,
        bookId: String,
        progressFraction: Float,
        bytesRead: Long,
        totalBytes: Long
    ): Notification {
        val percent = (progressFraction * 100).toInt().coerceIn(0, 100)

        // Main app launch intent
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Cancel download action intent
        val cancelIntent = Intent(this, BookDownloadService::class.java).apply {
            action = ACTION_CANCEL_DOWNLOAD
            putExtra(EXTRA_BOOK_ID, bookId)
        }
        val cancelPendingIntent = PendingIntent.getService(
            this,
            bookId.hashCode(),
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val sizeInfo = if (totalBytes > 0) {
            "$percent% • ${BookDownloadManager.formatFileSize(bytesRead)} / ${BookDownloadManager.formatFileSize(totalBytes)}"
        } else {
            "ڈاؤن لوڈ جاری ہے..."
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("📥 ڈاؤن لوڈ جاری ہے: $bookTitle")
            .setContentText(sizeInfo)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentIntent(contentPendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "منسوخ کریں", cancelPendingIntent)

        if (totalBytes > 0) {
            builder.setProgress(100, percent, false)
        } else {
            builder.setProgress(0, 0, true)
        }

        return builder.build()
    }

    private fun showCompletedNotification(bookId: String, bookTitle: String) {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            bookId.hashCode(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("✅ ڈاؤن لوڈ مکمل: $bookTitle")
            .setContentText("کتاب کامیابی سے محفوظ ہو گئی ہے - پڑھنے کے لیے ٹیپ کریں")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationId = NOTIFICATION_COMPLETED_BASE + (bookId.hashCode() and 0x7FFF)
        notificationManager.notify(notificationId, notification)
    }

    private fun showFailedNotification(bookId: String, bookTitle: String, errorMsg: String) {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            bookId.hashCode(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("❌ ڈاؤن لوڈ نامکمل: $bookTitle")
            .setContentText(errorMsg)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationId = NOTIFICATION_COMPLETED_BASE + (bookId.hashCode() and 0x7FFF)
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Book Downloads (کتب ڈاؤن لوڈز)",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "کتب کی بیک گراؤنڈ ڈاؤن لوڈنگ کا پروگریس نوٹیفکیشن"
                enableLights(false)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
