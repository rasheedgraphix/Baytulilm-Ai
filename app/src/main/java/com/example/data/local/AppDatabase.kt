package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.BookEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.RecentReadingEntity
import com.example.data.model.TasbeehRecordEntity

@Database(
    entities = [
        BookEntity::class,
        BookmarkEntity::class,
        RecentReadingEntity::class,
        TasbeehRecordEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun recentReadingDao(): RecentReadingDao
    abstract fun tasbeehDao(): TasbeehDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun migrateDatabase(db: SupportSQLiteDatabase) {
            // 1. Create tasbeeh_records table if it does not exist
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `tasbeeh_records` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `dhikrName` TEXT NOT NULL,
                    `count` INTEGER NOT NULL,
                    `target` INTEGER NOT NULL,
                    `timestamp` INTEGER NOT NULL
                )
                """.trimIndent()
            )

            // 2. Create bookmarks table if it does not exist
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `bookmarks` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bookId` TEXT NOT NULL,
                    `bookTitle` TEXT NOT NULL,
                    `pageNumber` INTEGER NOT NULL,
                    `note` TEXT NOT NULL
                )
                """.trimIndent()
            )

            // 3. Create recent_readings table if it does not exist
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `recent_readings` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bookId` TEXT NOT NULL,
                    `bookTitle` TEXT NOT NULL,
                    `author` TEXT NOT NULL,
                    `pageNumber` INTEGER NOT NULL,
                    `totalPages` INTEGER NOT NULL,
                    `timestamp` INTEGER NOT NULL
                )
                """.trimIndent()
            )

            // 4. Create books table if it does not exist
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `books` (
                    `id` TEXT NOT NULL PRIMARY KEY,
                    `title` TEXT NOT NULL,
                    `author` TEXT NOT NULL,
                    `darja` TEXT NOT NULL,
                    `subject` TEXT NOT NULL,
                    `lastReadPage` INTEGER NOT NULL DEFAULT 0,
                    `isFavorite` INTEGER NOT NULL DEFAULT 0,
                    `isBookmarked` INTEGER NOT NULL DEFAULT 0,
                    `isDownloaded` INTEGER NOT NULL DEFAULT 0,
                    `downloadProgress` REAL NOT NULL DEFAULT 0,
                    `language` TEXT NOT NULL DEFAULT '',
                    `titleUrdu` TEXT NOT NULL DEFAULT '',
                    `authorUrdu` TEXT NOT NULL DEFAULT '',
                    `type` TEXT NOT NULL DEFAULT '',
                    `description` TEXT NOT NULL DEFAULT '',
                    `descriptionUrdu` TEXT NOT NULL DEFAULT '',
                    `pdfUrl` TEXT NOT NULL DEFAULT '',
                    `pageCount` INTEGER NOT NULL DEFAULT 0,
                    `rating` REAL NOT NULL DEFAULT 0,
                    `coverResName` TEXT NOT NULL DEFAULT '',
                    `coverUrl` TEXT NOT NULL DEFAULT ''
                )
                """.trimIndent()
            )

            // 5. Ensure any missing columns on existing books table are safely added without losing any data
            val existingColumns = mutableSetOf<String>()
            try {
                db.query("PRAGMA table_info(`books`)").use { cursor ->
                    val nameIndex = cursor.getColumnIndex("name")
                    if (nameIndex != -1) {
                        while (cursor.moveToNext()) {
                            existingColumns.add(cursor.getString(nameIndex))
                        }
                    }
                }
            } catch (_: Exception) {}

            val columnsToAdd = mapOf(
                "lastReadPage" to "INTEGER NOT NULL DEFAULT 0",
                "isFavorite" to "INTEGER NOT NULL DEFAULT 0",
                "isBookmarked" to "INTEGER NOT NULL DEFAULT 0",
                "isDownloaded" to "INTEGER NOT NULL DEFAULT 0",
                "downloadProgress" to "REAL NOT NULL DEFAULT 0",
                "language" to "TEXT NOT NULL DEFAULT ''",
                "titleUrdu" to "TEXT NOT NULL DEFAULT ''",
                "authorUrdu" to "TEXT NOT NULL DEFAULT ''",
                "type" to "TEXT NOT NULL DEFAULT ''",
                "description" to "TEXT NOT NULL DEFAULT ''",
                "descriptionUrdu" to "TEXT NOT NULL DEFAULT ''",
                "pdfUrl" to "TEXT NOT NULL DEFAULT ''",
                "pageCount" to "INTEGER NOT NULL DEFAULT 0",
                "rating" to "REAL NOT NULL DEFAULT 0",
                "coverResName" to "TEXT NOT NULL DEFAULT ''",
                "coverUrl" to "TEXT NOT NULL DEFAULT ''"
            )

            for ((colName, colDef) in columnsToAdd) {
                if (!existingColumns.contains(colName)) {
                    try {
                        db.execSQL("ALTER TABLE `books` ADD COLUMN `$colName` $colDef")
                    } catch (_: Exception) {}
                }
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                migrateDatabase(db)
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                migrateDatabase(db)
            }
        }

        val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                migrateDatabase(db)
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "baytul_ilm_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_1_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
