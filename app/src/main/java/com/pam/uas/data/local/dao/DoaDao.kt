package com.pam.uas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pam.uas.data.local.entity.DoaEntity

@Dao
interface DoaDao {

    @Query("SELECT * FROM doa ORDER BY localId DESC")
    fun getAllDoa(): LiveData<List<DoaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoa(doa: DoaEntity)

    @Query("DELETE FROM doa")
    suspend fun deleteAll()
}
