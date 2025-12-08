package com.pam.uas.data.repository

import com.pam.uas.data.local.dao.DoaDao
import com.pam.uas.data.local.entity.DoaEntity
import com.pam.uas.data.remote.RetrofitClient

class DoaRepository(private val doaDao: DoaDao) {

    val allDoa = doaDao.getAllDoa()

    suspend fun refreshFromApi() {
        val response = RetrofitClient.instance.getAllDoa()

        if (response.isSuccessful) {
            response.body()?.let { apiList ->

                // kosongkan database dulu (opsional)
                doaDao.deleteAll()

                // simpan satu-satu
                for (item in apiList) {
                    val doaEntity = DoaEntity(
                        apiId = item.id,
                        judul = item.doa,
                        arab = item.ayat,
                        latin = item.latin,
                        arti = item.artinya
                    )
                    doaDao.insertDoa(doaEntity)
                }
            }
        }
    }
}
