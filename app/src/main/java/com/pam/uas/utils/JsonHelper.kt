package com.pam.uas.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pam.uas.data.local.entity.KisahNabiEntity
import com.pam.uas.data.local.entity.PembelajaranEntity
import java.io.IOException

class JsonHelper(private val context: Context) {

    fun loadKisahNabi(): List<KisahNabiEntity> {
        val jsonString: String
        try {
            jsonString = context.assets.open("kisah_nabi.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val listType = object : TypeToken<List<KisahNabiEntity>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    fun loadPembelajaran(fileName: String): List<PembelajaranEntity> {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }

        // Langsung convert ke Entity karena struktur JSON sudah cocok
        val listType = object : TypeToken<List<PembelajaranEntity>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}
