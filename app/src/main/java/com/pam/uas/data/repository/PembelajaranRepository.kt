package com.pam.uas.data.repository

import androidx.lifecycle.LiveData
import com.pam.uas.data.local.dao.PembelajaranDao
import com.pam.uas.data.local.entity.PembelajaranEntity

class PembelajaranRepository(private val dao: PembelajaranDao) {

    fun getMateriByKategori(kategori: String): LiveData<List<PembelajaranEntity>> {
        return dao.getMateriByKategori(kategori)
    }

    suspend fun insertAll(list: List<PembelajaranEntity>) {
        dao.insertAll(list)
    }

    suspend fun isEmpty(): Boolean {
        return dao.getCount() == 0
    }
}
