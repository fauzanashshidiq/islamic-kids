package com.pam.uas.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.pam.uas.data.local.database.AppDatabase
import com.pam.uas.data.local.entity.DoaEntity
import com.pam.uas.data.remote.RetrofitClient
import com.pam.uas.data.remote.response.ApiDoaResponse
import com.pam.uas.data.repository.DoaRepository
import kotlinx.coroutines.launch

class DoaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: DoaRepository

    val apiDoaList = MutableLiveData<List<ApiDoaResponse>>()
    val savedDoa = MutableLiveData<List<DoaEntity>>()

    init {
        val context = application.applicationContext
        val api = RetrofitClient.instance
        val db = AppDatabase.getDatabase(context)
        repo = DoaRepository(api, db.doaDao())
    }

    fun loadApiDoa() = viewModelScope.launch {
        apiDoaList.postValue(repo.fetchApiDoa())
    }

    fun saveDoa(apiDoa: ApiDoaResponse) = viewModelScope.launch {
        val entity = DoaEntity(
            id = 0, // Auto generate
            doa = apiDoa.doa,
            ayat = apiDoa.ayat,
            latin = apiDoa.latin,
            artinya = apiDoa.artinya,
            isMemorized = false
        )
        repo.insertMany(listOf(entity))
    }


    fun loadSavedDoa() = viewModelScope.launch {
        savedDoa.postValue(repo.getSavedDoa())
    }

    fun updateMemorizedStatus(doa: DoaEntity, isMemorized: Boolean) {
        viewModelScope.launch {
            repo.updateMemorizedStatus(doa.id, isMemorized)
        }
    }

    fun deleteDoaByJudul(judul: String) = viewModelScope.launch {
        // Kamu perlu menambahkan fungsi ini di Repository dan DAO
        repo.deleteByJudul(judul)
    }
}

