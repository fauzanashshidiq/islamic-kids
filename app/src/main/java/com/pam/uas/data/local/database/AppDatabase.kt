package com.pam.uas.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pam.uas.data.local.dao.DoaDao
import com.pam.uas.data.local.dao.KisahNabiDao
import com.pam.uas.data.local.entity.DoaEntity
import com.pam.uas.data.local.entity.KisahNabiEntity

@Database(entities = [DoaEntity::class, KisahNabiEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun doaDao(): DoaDao
    abstract fun kisahNabiDao(): KisahNabiDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "islami_db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
