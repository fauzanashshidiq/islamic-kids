package com.pam.uas.sfx

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException

object SfxPlayer {

    enum class SoundType(val fileName: String) {
        POP("sfx/pop.mp3"),
        SUCCESS("sfx/success.mp3"),
        CUTE("sfx/cute.mp3"),
        CLANG("sfx/clang.mp3"),
        TRING("sfx/tring.mp3"),
        ERROR("sfx/error.mp3"),
    }

    // Fungsi utama untuk memutar suara
    fun play(context: Context, soundType: SoundType) {
        try {
            val afd = context.assets.openFd(soundType.fileName)

            val mp = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()
            }

            mp.setOnCompletionListener { player ->
                player.release()
            }

            afd.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
