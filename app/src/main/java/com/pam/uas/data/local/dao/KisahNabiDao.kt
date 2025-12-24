package com.pam.uas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pam.uas.data.local.entity.KisahNabiEntity

@Dao
interface KisahNabiDao {
    @Query("SELECT * FROM kisah_nabi")
    fun getAllKisah(): LiveData<List<KisahNabiEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kisahList: List<KisahNabiEntity>)

    @Query("SELECT COUNT(*) FROM kisah_nabi")
    suspend fun getCount(): Int
}
