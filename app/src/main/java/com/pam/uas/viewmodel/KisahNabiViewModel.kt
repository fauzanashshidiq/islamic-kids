package com.pam.uas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.pam.uas.data.local.database.AppDatabase
import com.pam.uas.data.local.entity.KisahNabiEntity
import com.pam.uas.data.repository.KisahNabiRepository
import com.pam.uas.utils.JsonHelper

class KisahNabiViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: KisahNabiRepository

    val kisahNabiList: LiveData<List<KisahNabiEntity>>

    init {
        val db = AppDatabase.getDatabase(application)

        val dao = db.kisahNabiDao()

        repo = KisahNabiRepository(dao)

        kisahNabiList = repo.allKisahNabi
    }

    fun preloadKisahNabi() = viewModelScope.launch {
        if (repo.isKisahNabiEmpty()) {
            val helper = JsonHelper(getApplication())
            val data = helper.loadKisahNabi()
            if (data.isNotEmpty()) {
                repo.insertKisahNabi(data)
            }
        }
    }
}