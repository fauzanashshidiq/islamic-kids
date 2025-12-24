package com.pam.uas.data.repository

import androidx.lifecycle.LiveData
import com.pam.uas.data.local.dao.PembelajaranDao
import com.pam.uas.data.local.entity.PembelajaranEntity

class PembelajaranRepository(private val dao: PembelajaranDao) {

    // Mengambil data berdasarkan kategori untuk ditampilkan di UI
    fun getMateriByKategori(kategori: String): LiveData<List<PembelajaranEntity>> {
        return dao.getMateriByKategori(kategori)
    }

    // Memasukkan banyak data sekaligus (Batch Insert)
    suspend fun insertAll(list: List<PembelajaranEntity>) {
        dao.insertAll(list)
    }

    // Cek apakah tabel pembelajaran kosong (Logic untuk Preload)
    // Asumsi di DAO kamu sudah ada fungsi @Query("SELECT COUNT(*) FROM pembelajaran") suspend fun getCount(): Int
    suspend fun isEmpty(): Boolean {
        return dao.getCount() == 0
    }
}
