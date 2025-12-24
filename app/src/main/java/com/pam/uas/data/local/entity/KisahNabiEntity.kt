package com.pam.uas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "kisah_nabi")
data class KisahNabiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("thn_kelahiran")
    val thnKelahiran: Int,

    @SerializedName("usia")
    val usia: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("tmp")
    val tmp: String,

    // Opsional: Image url jika ada, atau flag sudah dibaca
    val isRead: Boolean = false
)
