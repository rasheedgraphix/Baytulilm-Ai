package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.BookEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.RecentReadingEntity
import com.example.data.model.TasbeehRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isFavorite = 1")
    fun getFavoriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isBookmarked = 1")
    fun getBookmarkedBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isDownloaded = 1")
    fun getDownloadedBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE darja = :darja")
    fun getBooksByDarja(darja: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE subject = :subject")
    fun getBooksBySubject(subject: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBook(id: String)

    @Query("UPDATE books SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: String, favorite: Boolean)

    @Query("UPDATE books SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setBookmarked(id: String, bookmarked: Boolean)

    @Query("UPDATE books SET lastReadPage = :page WHERE id = :id")
    suspend fun updateLastReadPage(id: String, page: Int)

    @Query("UPDATE books SET isDownloaded = :isDownloaded, downloadProgress = :progress WHERE id = :id")
    suspend fun updateDownloadStatus(id: String, isDownloaded: Boolean, progress: Float)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)
}

@Dao
interface RecentReadingDao {
    @Query("SELECT * FROM recent_readings ORDER BY timestamp DESC")
    fun getRecentReadings(): Flow<List<RecentReadingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentReading(recentReading: RecentReadingEntity)
}

@Dao
interface TasbeehDao {
    @Query("SELECT * FROM tasbeeh_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<TasbeehRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: TasbeehRecordEntity)
}
