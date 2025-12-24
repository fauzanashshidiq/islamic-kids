package com.pam.uas.utils

object AudioMapper {
    private val audioMap = mapOf(
        "Doa sebelum tidur" to "audio_doa_tidur",
        "Doa bangun tidur" to "audio_bangun_tidur",
        "Doa masuk kamar mandi" to "audio_masuk_wc",
        "Doa ketika bercermin" to "audio_bercermin",
        "Doa keluar rumah" to "audio_keluar_rumah",
        "Doa masuk rumah" to "audio_masuk_rumah",
        "Doa memohon ilmu yang bermanfaat" to "audio_ilmu_bermanfaat",
        "Doa sebelum belajar" to "audio_sebelum_belajar",
        "Doa sesudah belajar" to "audio_sesudah_belajar",
        "Doa sebelum wudhu" to "audio_sebelum_wudhu",
        "Doa setelah wudhu" to "audio_setelah_wudhu",
        "Doa sebelum membaca al-qur'an" to "audio_sebelum_baca_quran",
        "Doa setelah membaca al-qur'an" to "audio_setelah_baca_quran",
        "Doa sebelum mandi" to "audio_sebelum_mandi",
        "Doa hendak berpergian" to "audio_bepergian",
        "Doa ketika sampai di tempat tujuan" to "audio_sampai_tujuan",
        "Doa memakai pakaian" to "audio_memakai_pakaian",
        "Doa memakai pakaian baru" to "audio_pakaian_baru",
        "Doa melepas pakaian" to "audio_melepas_pakaian",
        "Doa menjelang sholat subuh" to "audio_fajar_subuh",
        "Doa menyambut pagi hari" to "audio_pagi_hari",
        "Doa menyambut sore hari" to "audio_sore_hari",
        "Doa bercermin" to "audio_bercermin_alt",
        "Doa pergi ke masjid" to "audio_ke_masjid",
        "Doa masuk masjid" to "audio_masuk_masjid",
        "Doa keluar masjid" to "audio_keluar_masjid",
        "Doa setelah adzan" to "audio_setelah_adzan",
        "Doa naik kendaraan" to "audio_naik_kendaraan",
        "Doa masuk pasar" to "audio_masuk_pasar",
        "Doa sebelum makan" to "audio_sebelum_makan",
        "Doa sesudah makan" to "audio_sesudah_makan",
        "Doa ketika turun hujan" to "audio_turun_hujan",
        "Doa ketika takut bahaya hujan lebat" to "audio_hujan_lebat",
        "Doa setelah turun hujan" to "audio_setelah_hujan",
        "Doa bertemu binatang buas" to "audio_binatang_buas",
        "Doa agar selalu dicukupkan rezeki" to "audio_cukup_rezeki",
        "Doa ketika menyisir rambut" to "audio_menyisir_rambut"
    )

    fun getAudioFile(judulDoa: String): String {
        // Mengembalikan nama file jika ada, atau string kosong jika tidak ada
        return audioMap[judulDoa] ?: ""
    }
}
