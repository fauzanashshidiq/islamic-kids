package com.pam.uas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doa")
data class DoaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val doa: String,
    val ayat: String,
    val latin: String,
    val artinya: String,
    var isMemorized: Boolean = false,
    val catatan: String? = "",
    val voice_path: String? = ""
)