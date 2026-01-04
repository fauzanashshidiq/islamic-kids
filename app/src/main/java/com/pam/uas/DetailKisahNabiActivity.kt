package com.pam.uas

import android.content.res.AssetFileDescriptor
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.bumptech.glide.Glide
import com.pam.uas.databinding.ActivityDetailKisahNabiBinding
import com.pam.uas.sfx.SfxPlayer
import java.io.IOException

class DetailKisahNabiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailKisahNabiBinding

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKisahNabiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackgroundMusic()

        binding.btnBack.setOnClickListener { view ->
            SfxPlayer.play(view.context, SfxPlayer.SoundType.POP)
            view.animate()
                .scaleX(0.85f)
                .scaleY(0.85f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setInterpolator(android.view.animation.BounceInterpolator())
                        .withEndAction {
                            finish()
                        }
                        .start()
                }
                .start()
        }

        val nama = intent.getStringExtra("EXTRA_NAMA") ?: "Nama Nabi"
        val usia = intent.getStringExtra("EXTRA_USIA") ?: "-"
        val tempat = intent.getStringExtra("EXTRA_TMP") ?: "-"
        val tahun = intent.getStringExtra("EXTRA_THN") ?: "-"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "Tidak ada deskripsi."

        binding.tvDetailNama.text = nama
        binding.tvUsia.text = "$usia Tahun"
        binding.tvTempat.text = tempat
        binding.tvTahun.text = "$tahun SM"
        binding.tvDeskripsi.text = desc

        val context = this
        val warnaResId = when {
            nama.contains("Adam", ignoreCase = true) -> R.color.color_kisah_blue
            nama.contains("Idris", ignoreCase = true) -> R.color.color_kisah_green
            nama.contains("Nuh", ignoreCase = true) -> R.color.color_kisah_yellow
            nama.contains("Hud", ignoreCase = true) -> R.color.color_kisah_purple
            nama.contains("Sholeh", ignoreCase = true) -> R.color.color_kisah_red
            nama.contains("Ibrahim", ignoreCase = true) -> R.color.color_kisah_blue
            nama.contains("Luth", ignoreCase = true) -> R.color.color_kisah_green
            nama.contains("Ismail", ignoreCase = true) -> R.color.color_kisah_yellow
            nama.contains("Ishaq", ignoreCase = true) -> R.color.color_kisah_purple
            nama.contains("Yaqub", ignoreCase = true) -> R.color.color_kisah_red
            nama.contains("Yusuf", ignoreCase = true) -> R.color.color_kisah_blue
            nama.contains("Ayyub", ignoreCase = true) -> R.color.color_kisah_green
            nama.contains("Syu'aib", ignoreCase = true) -> R.color.color_kisah_yellow
            nama.contains("Musa", ignoreCase = true) -> R.color.color_kisah_purple
            nama.contains("Harun", ignoreCase = true) -> R.color.color_kisah_red
            nama.contains("Dzulkifli", ignoreCase = true) -> R.color.color_kisah_blue
            nama.contains("Daud", ignoreCase = true) -> R.color.color_kisah_green
            nama.contains("Sulaiman", ignoreCase = true) -> R.color.color_kisah_yellow
            nama.contains("Ilyasa'", ignoreCase = true) -> R.color.color_kisah_red
            nama.contains("Ilyas", ignoreCase = true) -> R.color.color_kisah_purple
            nama.contains("Yunus", ignoreCase = true) -> R.color.color_kisah_blue
            nama.contains("Zakariya", ignoreCase = true) -> R.color.color_kisah_green
            nama.contains("Yahya", ignoreCase = true) -> R.color.color_kisah_yellow
            nama.contains("Isa", ignoreCase = true) -> R.color.color_kisah_purple
            nama.contains("Muhammad", ignoreCase = true) -> R.color.color_kisah_red
            else -> R.color.white
        }

        val colorMain = ContextCompat.getColor(context, warnaResId)
        val colorShadow = ColorUtils.blendARGB(colorMain, Color.BLACK, 0.2f)

        binding.layoutHeaderMain.background.setTint(colorMain)
        binding.viewHeaderShadow.background.setTint(colorShadow)
    }

    private fun setupBackgroundMusic() {
        if (mediaPlayer != null) return

        try {
            mediaPlayer = MediaPlayer()
            val afd: AssetFileDescriptor = assets.openFd("music/bg_music2.mp3")

            mediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()

            mediaPlayer?.prepare()
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(0.5f, 0.5f)

            mediaPlayer?.start()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        } else if (mediaPlayer == null) {
            setupBackgroundMusic()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
