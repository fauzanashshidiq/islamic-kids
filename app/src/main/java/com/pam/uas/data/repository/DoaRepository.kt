package com.pam.uas.data.repository

import com.pam.uas.data.local.dao.DoaDao
import com.pam.uas.data.local.entity.DoaEntity
import com.pam.uas.data.remote.api.ApiService
import com.pam.uas.data.remote.response.ApiDoaResponse

class DoaRepository(
    private val api: ApiService,
    private val dao: DoaDao
) {

    // FETCH DOA FROM API
    suspend fun fetchApiDoa(): List<ApiDoaResponse> {
        val response = api.getAllDoa()

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("API error: ${response.code()} - ${response.message()}")
        }
    }

    // INSERT one selected doa into ROOM
    suspend fun insert(doa: DoaEntity) = dao.insertDoa(doa)

    // INSERT many
    suspend fun insertMany(list: List<DoaEntity>) {
        list.forEach { dao.insertDoa(it) }
    }

    // GET all saved doa from ROOM
    suspend fun getSavedDoa(): List<DoaEntity> {
        return dao.getAllDoa()
    }

    suspend fun updateMemorizedStatus(id: Int, isMemorized: Boolean) {
        dao.updateMemorizedStatus(id, isMemorized)
    }

    suspend fun deleteByJudul(judul: String) {
        dao.deleteByJudul(judul)    }

}
