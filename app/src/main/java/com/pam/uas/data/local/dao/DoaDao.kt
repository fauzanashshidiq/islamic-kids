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

    @Query("UPDATE doa SET isMemorized = :isMemorized WHERE id = :id")
    suspend fun updateMemorizedStatus(id: Int, isMemorized: Boolean)

    @Query("DELETE FROM doa WHERE doa = :judul")
    suspend fun deleteByJudul(judul: String)

    @Query("UPDATE doa SET catatan = :catatan WHERE doa = :judul")
    suspend fun updateCatatanByJudul(judul: String, catatan: String)
}
