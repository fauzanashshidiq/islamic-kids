package com.pam.uas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doa")
data class DoaEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,

    val apiId: String,
    val judul: String,
    val arab: String,
    val latin: String,
    val arti: String
)

