package com.example.data.repository

import com.example.data.local.BookDao
import com.example.data.local.BookmarkDao
import com.example.data.local.RecentReadingDao
import com.example.data.local.TasbeehDao
import com.example.data.model.BookEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.RecentReadingEntity
import com.example.data.model.TasbeehRecordEntity
import kotlinx.coroutines.flow.Flow

class BookRepository(
    private val bookDao: BookDao,
    private val bookmarkDao: BookmarkDao,
    private val recentReadingDao: RecentReadingDao,
    private val tasbeehDao: TasbeehDao
) {
    val allBooks: Flow<List<BookEntity>> = bookDao.getAllBooks()
    val favoriteBooks: Flow<List<BookEntity>> = bookDao.getFavoriteBooks()
    val bookmarkedBooks: Flow<List<BookEntity>> = bookDao.getBookmarkedBooks()
    val downloadedBooks: Flow<List<BookEntity>> = bookDao.getDownloadedBooks()
    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
    val recentReadings: Flow<List<RecentReadingEntity>> = recentReadingDao.getRecentReadings()
    val tasbeehRecords: Flow<List<TasbeehRecordEntity>> = tasbeehDao.getAllRecords()

    suspend fun seedInitialDataIfEmpty() {
        // Simple seed check
        val books = InitialDataSeed.sampleBooks
        bookDao.insertAll(books)
        bookDao.deleteBook("rabia_al_miraat_maqamat")
    }

    suspend fun insertBooks(books: List<BookEntity>) {
        bookDao.insertAll(books)
    }

    fun getBooksByDarja(darja: String): Flow<List<BookEntity>> = bookDao.getBooksByDarja(darja)
    fun getBooksBySubject(subject: String): Flow<List<BookEntity>> = bookDao.getBooksBySubject(subject)
    fun searchBooks(query: String): Flow<List<BookEntity>> = bookDao.searchBooks(query)

    suspend fun getBookById(id: String): BookEntity? = bookDao.getBookById(id)

    suspend fun insertBook(book: BookEntity) {
        bookDao.insertBook(book)
    }

    suspend fun deleteBook(id: String) {
        bookDao.deleteBook(id)
    }

    suspend fun toggleFavorite(id: String, currentStatus: Boolean) {
        bookDao.setFavorite(id, !currentStatus)
    }

    suspend fun toggleBookmark(id: String, currentStatus: Boolean) {
        bookDao.setBookmarked(id, !currentStatus)
    }

    suspend fun savePageBookmark(bookId: String, bookTitle: String, pageNumber: Int, note: String) {
        bookmarkDao.insertBookmark(
            BookmarkEntity(
                bookId = bookId,
                bookTitle = bookTitle,
                pageNumber = pageNumber,
                note = note
            )
        )
    }

    suspend fun recordReadingProgress(bookId: String, bookTitle: String, author: String, pageNumber: Int, totalPages: Int) {
        bookDao.updateLastReadPage(bookId, pageNumber)
        recentReadingDao.insertRecentReading(
            RecentReadingEntity(
                bookId = bookId,
                bookTitle = bookTitle,
                author = author,
                pageNumber = pageNumber,
                totalPages = totalPages
            )
        )
    }

    suspend fun setDownloadStatus(bookId: String, isDownloaded: Boolean, progress: Float) {
        bookDao.updateDownloadStatus(bookId, isDownloaded, progress)
    }

    suspend fun addTasbeehRecord(dhikrName: String, count: Int, target: Int) {
        tasbeehDao.insertRecord(
            TasbeehRecordEntity(
                dhikrName = dhikrName,
                count = count,
                target = target
            )
        )
    }
}
