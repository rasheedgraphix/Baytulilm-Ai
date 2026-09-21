package com.example.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.data.model.BookEntity
import com.example.service.BookDownloadService
import com.example.ui.viewmodel.BookDownloadProgress
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object BookDownloadManager {
    private val _activeDownloads = MutableStateFlow<Map<String, BookDownloadProgress>>(emptyMap())
    val activeDownloads: StateFlow<Map<String, BookDownloadProgress>> = _activeDownloads.asStateFlow()

    private val _downloadCompletedEvent = MutableSharedFlow<String>(extraBufferCapacity = 5)
    val downloadCompletedEvent: SharedFlow<String> = _downloadCompletedEvent.asSharedFlow()

    fun startDownload(context: Context, book: BookEntity) {
        // Immediate optimistic UI update
        _activeDownloads.update { current ->
            current + (book.id to BookDownloadProgress(
                bookId = book.id,
                isDownloading = true,
                progress = 0f,
                isCancelled = false,
                isCompleted = false
            ))
        }

        val intent = Intent(context, BookDownloadService::class.java).apply {
            action = BookDownloadService.ACTION_START_DOWNLOAD
            putExtra(BookDownloadService.EXTRA_BOOK_ID, book.id)
            putExtra(BookDownloadService.EXTRA_BOOK_TITLE, book.title)
            putExtra(BookDownloadService.EXTRA_PDF_URL, book.pdfUrl)
        }
        try {
            ContextCompat.startForegroundService(context, intent)
        } catch (e: Exception) {
            try {
                context.startService(intent)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun cancelDownload(context: Context, bookId: String) {
        _activeDownloads.update { current ->
            val existing = current[bookId]
            if (existing != null) {
                current + (bookId to existing.copy(
                    isDownloading = false,
                    isCancelled = true,
                    progress = 0f,
                    errorMessage = "Download cancelled"
                ))
            } else {
                current
            }
        }

        val intent = Intent(context, BookDownloadService::class.java).apply {
            action = BookDownloadService.ACTION_CANCEL_DOWNLOAD
            putExtra(BookDownloadService.EXTRA_BOOK_ID, bookId)
        }
        try {
            context.startService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateProgress(progress: BookDownloadProgress) {
        _activeDownloads.update { current ->
            current + (progress.bookId to progress)
        }
        if (progress.isCompleted) {
            _downloadCompletedEvent.tryEmit(progress.bookId)
        }
    }

    fun removeDownload(bookId: String) {
        _activeDownloads.update { current ->
            current - bookId
        }
    }

    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 MB"
        val mb = bytes.toDouble() / (1024.0 * 1024.0)
        return if (mb >= 0.1) {
            String.format(java.util.Locale.US, "%.1f MB", mb)
        } else {
            val kb = bytes.toDouble() / 1024.0
            String.format(java.util.Locale.US, "%.0f KB", kb)
        }
    }
}
