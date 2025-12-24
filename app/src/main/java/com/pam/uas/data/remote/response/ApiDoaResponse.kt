package com.pam.uas.data.remote.response

data class ApiDoaResponse(
    val id: String,
    val doa: String,
    val ayat: String,
    val latin: String,
    val artinya: String,
    var isChecked: Boolean = false
)