package com.pam.uas.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.pam.uas.data.local.database.AppDatabase
import com.pam.uas.data.local.entity.DoaEntity
import com.pam.uas.data.remote.RetrofitClient
import com.pam.uas.data.remote.response.ApiDoaResponse
import com.pam.uas.data.repository.DoaRepository
import com.pam.uas.utils.AudioMapper
import kotlinx.coroutines.launch

class DoaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: DoaRepository

    val apiDoaList = MutableLiveData<List<ApiDoaResponse>>()
    val savedDoa = MutableLiveData<List<DoaEntity>>()

    // Master data (Semua data dari DB)
    private val _allSavedDoa = MutableLiveData<List<DoaEntity>>()

    // Data yang ditampilkan ke UI (bisa difilter)
    val displayDoaList = MediatorLiveData<List<DoaEntity>>()

    val isError = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()

    private var currentFilterMode = FilterMode.ALL

    enum class FilterMode {
        ALL, MEMORIZED, NOT_MEMORIZED
    }

    init {
        val context = application.applicationContext
        val api = RetrofitClient.instance
        val db = AppDatabase.getDatabase(context)
        repo = DoaRepository(api, db.doaDao())

        // Hubungkan display list dengan data asli
        displayDoaList.addSource(_allSavedDoa) { list ->
            applyFilter(list)
        }
    }

    fun setFilter(mode: FilterMode) {
        currentFilterMode = mode
        // Trigger ulang filter dengan data yang ada sekarang
        _allSavedDoa.value?.let { applyFilter(it) }
    }

    private fun applyFilter(list: List<DoaEntity>) {
        val filteredList = when (currentFilterMode) {
            FilterMode.ALL -> list
            FilterMode.MEMORIZED -> list.filter { it.isMemorized }
            FilterMode.NOT_MEMORIZED -> list.filter { !it.isMemorized }
        }
        displayDoaList.value = filteredList
    }

    fun loadApiDoa() = viewModelScope.launch {
        // 1. Mulai Loading
        isLoading.postValue(true)
        isError.postValue(false) // Reset error

        try {
            // 2. Coba ambil data
            val result = repo.fetchApiDoa()

            // Urutkan hasil berdasarkan nama 'doa' secara abjad (A-Z)
            val sortedResult = result.sortedBy { it.doa }

            // Masukkan data yang sudah urut
            apiDoaList.postValue(sortedResult)
            isError.postValue(false)
        } catch (e: Exception) {
            // 3. Jika GAGAL (No Internet / Server Down)
            e.printStackTrace()
            isError.postValue(true) // Beritahu UI ada error
            apiDoaList.postValue(emptyList())

        } finally {
            // 4. Selesai Loading
            isLoading.postValue(false)
        }
    }

    fun saveDoa(apiDoa: ApiDoaResponse, catatanAwal: String = "") = viewModelScope.launch {
        val audioFile = AudioMapper.getAudioFile(apiDoa.doa)

        val entity = DoaEntity(
            id = 0, // Auto generate
            doa = apiDoa.doa,
            ayat = apiDoa.ayat,
            latin = apiDoa.latin,
            artinya = apiDoa.artinya,
            isMemorized = false,
            catatan = catatanAwal,
            voice_path = audioFile
        )
        repo.insertMany(listOf(entity))
    }


    fun loadSavedDoa() = viewModelScope.launch {
        val list = repo.getSavedDoa()
        savedDoa.postValue(list)
        _allSavedDoa.postValue(list)
    }

    fun updateMemorizedStatus(doa: DoaEntity, isMemorized: Boolean) {
        viewModelScope.launch {
            repo.updateMemorizedStatus(doa.id, isMemorized)
            val currentList = _allSavedDoa.value?.toMutableList()
            currentList?.find { it.id == doa.id }?.isMemorized = isMemorized
            _allSavedDoa.value = currentList ?: emptyList()
        }
    }

    fun deleteDoaByJudul(judul: String) = viewModelScope.launch {
        // Kamu perlu menambahkan fungsi ini di Repository dan DAO
        repo.deleteByJudul(judul)
    }

    fun updateCatatan(judul: String, catatan: String) = viewModelScope.launch {
        repo.updateCatatan(judul, catatan)
    }
}

