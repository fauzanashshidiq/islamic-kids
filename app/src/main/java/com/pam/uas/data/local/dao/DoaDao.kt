package com.pam.uas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pam.uas.data.local.entity.DoaEntity

@Dao
interface DoaDao {

    @Insert
    suspend fun insertDoa(doa: DoaEntity)

    @Insert
    suspend fun insertMany(list: List<DoaEntity>)

    @Query("SELECT * FROM doa")
    suspend fun getAllDoa(): List<DoaEntity>
}
