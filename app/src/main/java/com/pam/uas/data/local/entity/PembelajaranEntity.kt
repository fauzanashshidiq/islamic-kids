package com.pam.uas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pembelajaran")
    data class PembelajaranEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val kategori: String,
        val urutan: Int,
        val nama: String,
        val teks_arab: String? = "",
        val deskripsi: String,
        val keterangan: String? = "",
        val image_path: String? = "",
        val voice_path: String? = ""
)
