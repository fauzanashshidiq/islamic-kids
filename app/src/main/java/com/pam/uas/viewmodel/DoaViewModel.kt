package com.pam.uas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pam.uas.data.local.database.AppDatabase
import com.pam.uas.data.repository.DoaRepository
import kotlinx.coroutines.launch

class DoaViewModel(application: Application) : AndroidViewModel(application) {

    private val doaDao = AppDatabase.getDatabase(application).doaDao()
    private val repo = DoaRepository(doaDao)

    val listDoa = repo.allDoa

    fun loadFromApi() = viewModelScope.launch {
        android.util.Log.d("DoaVM", "Memanggil API...")
        repo.refreshFromApi()
    }
}
