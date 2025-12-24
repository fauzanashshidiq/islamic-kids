package com.pam.uas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pam.uas.data.local.entity.PembelajaranEntity

@Dao
interface PembelajaranDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pembelajaranList: List<PembelajaranEntity>)

    @Query("SELECT * FROM pembelajaran WHERE kategori = :cat ORDER BY urutan ASC")
    fun getMateriByKategori(cat: String): LiveData<List<PembelajaranEntity>>

    @Query("SELECT COUNT(*) FROM pembelajaran")
    suspend fun getCount(): Int

}