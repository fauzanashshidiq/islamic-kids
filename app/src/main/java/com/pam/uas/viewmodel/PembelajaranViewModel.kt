package com.pam.uas.viewmodel

import android.app.Application
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pam.uas.data.local.database.AppDatabase
import com.pam.uas.data.local.entity.PembelajaranEntity
import com.pam.uas.data.repository.PembelajaranRepository
import com.pam.uas.utils.JsonHelper

class PembelajaranViewModel(application: Application) : AndroidViewModel(application) {
    private val pembelajaranRepo: PembelajaranRepository

    init {
        val db = AppDatabase.getDatabase(application)

        // ... init dao lain ...

        // Init Pembelajaran Repo
        val pembelajaranDao = db.pembelajaranDao()
        pembelajaranRepo = PembelajaranRepository(pembelajaranDao)
    }

    // --- FUNGSI UNTUK UI ---
    // Dipanggil saat user masuk ke menu tertentu (misal: Rukun Islam)
    fun getMateri(kategori: String): LiveData<List<PembelajaranEntity>> {
        return pembelajaranRepo.getMateriByKategori(kategori)
    }

    // --- FUNGSI PRELOAD (Jalan sekali saja saat awal) ---
    fun preloadPembelajaran() = viewModelScope.launch {
        // Cek database dulu supaya tidak duplikat data
        if (pembelajaranRepo.isEmpty()) {
            val helper = JsonHelper(getApplication())
            val allData = mutableListOf<PembelajaranEntity>()

            // Load semua file JSON yang ada di assets
            try {
                allData.addAll(helper.loadPembelajaran("rukun_islam.json"))
                allData.addAll(helper.loadPembelajaran("rukun_iman.json"))
                allData.addAll(helper.loadPembelajaran("asmaul_husna.json"))
                allData.addAll(helper.loadPembelajaran("shalat_wajib.json"))
                allData.addAll(helper.loadPembelajaran("sifat_wajib_allah.json"))
                allData.addAll(helper.loadPembelajaran("tata_cara_wudhu.json"))

                // Simpan ke database jika data berhasil diload
                if (allData.isNotEmpty()) {
                    pembelajaranRepo.insertAll(allData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}