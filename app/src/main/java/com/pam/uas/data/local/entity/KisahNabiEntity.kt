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
    val thnKelahiran: String,

    @SerializedName("usia")
    val usia: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("tmp")
    val tmp: String,

    @SerializedName("icon_url")
    val iconUrl: String? = null,

    val isRead: Boolean = false
)
