package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 2,
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

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "baytul_ilm_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
