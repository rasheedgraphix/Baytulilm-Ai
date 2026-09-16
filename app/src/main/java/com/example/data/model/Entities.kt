package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val darja: String,
    val subject: String,
    val lastReadPage: Int = 0,
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val language: String = "",
    val titleUrdu: String = "",
    val authorUrdu: String = "",
    val type: String = "",
    val description: String = "",
    val descriptionUrdu: String = "",
    val pdfUrl: String = "",
    val pageCount: Int = 0,
    val rating: Float = 0f,
    val coverResName: String = "",
    val coverUrl: String = ""
) {
    fun getDisplayName(langCode: String): String {
        return if ((langCode == "ps" || langCode == "ur") && titleUrdu.isNotBlank()) titleUrdu else title
    }
}

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val bookTitle: String,
    val pageNumber: Int,
    val note: String
)

@Entity(tableName = "recent_readings")
data class RecentReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val bookTitle: String,
    val author: String,
    val pageNumber: Int,
    val totalPages: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbeeh_records")
data class TasbeehRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dhikrName: String,
    val count: Int,
    val target: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "islamic_favorites")
data class IslamicFavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val featureType: String,
    val itemId: Int
)
