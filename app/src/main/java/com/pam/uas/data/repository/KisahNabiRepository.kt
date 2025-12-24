package com.pam.uas.data.repository

import androidx.lifecycle.LiveData
import com.pam.uas.data.local.dao.KisahNabiDao
import com.pam.uas.data.local.entity.KisahNabiEntity

class KisahNabiRepository(
    private val kisahNabiDao: KisahNabiDao
) {
    val allKisahNabi: LiveData<List<KisahNabiEntity>> = kisahNabiDao.getAllKisah()

    suspend fun insertKisahNabi(list: List<KisahNabiEntity>) {
        kisahNabiDao.insertAll(list)
    }

    suspend fun isKisahNabiEmpty(): Boolean {
        return kisahNabiDao.getCount() == 0
    }
}