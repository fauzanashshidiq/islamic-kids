package com.pam.uas.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pam.uas.data.local.dao.DoaDao
import com.pam.uas.data.local.entity.DoaEntity

@Database(
    entities = [DoaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun doaDao(): DoaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "doa_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
