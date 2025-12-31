package com.pam.uas.sfx

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException

// Kita gunakan 'object' agar menjadi Singleton (satu instance untuk seluruh aplikasi)
object SfxPlayer {

    // Enum untuk jenis suara agar lebih rapi dan aman dari salah ketik
    enum class SoundType(val fileName: String) {
        POP("sfx/pop.mp3"),
        SUCCESS("sfx/success.mp3"),
        // Tambahkan jenis suara lain di sini jika perlu
        // ERROR("sfx/error.mp3")
    }

    // Fungsi utama untuk memutar suara
    fun play(context: Context, soundType: SoundType) {
        try {
            // Buka file dari folder assets berdasarkan enum
            val afd = context.assets.openFd(soundType.fileName)

            val mp = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()
            }

            // PENTING: Hapus MediaPlayer dari memori setelah selesai dimainkan
            mp.setOnCompletionListener { player ->
                player.release()
            }

            // Tutup AssetFileDescriptor
            afd.close()

        } catch (e: IOException) {
            e.printStackTrace()
            // Optional: Tambahkan log atau toast untuk debugging jika file tidak ditemukan
        }
    }
}
