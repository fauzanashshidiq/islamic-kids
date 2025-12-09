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

    fun saveSelectedDoa(list: List<ApiDoaResponse>) = viewModelScope.launch {
        val entities = list.map {
            DoaEntity(
                id = 0,
                doa = it.doa,
                ayat = it.ayat,
                latin = it.latin,
                artinya = it.artinya
            )
        }
        repo.insertMany(entities)
    }

    fun loadSavedDoa() = viewModelScope.launch {
        savedDoa.postValue(repo.getSavedDoa())
    }

}

