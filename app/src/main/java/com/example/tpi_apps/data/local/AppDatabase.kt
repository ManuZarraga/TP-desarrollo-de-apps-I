package com.example.tpi_apps.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tpi_apps.data.local.dao.ReviewDao
import com.example.tpi_apps.data.local.entities.ReviewEntity

@Database(entities = [ReviewEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_review_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
