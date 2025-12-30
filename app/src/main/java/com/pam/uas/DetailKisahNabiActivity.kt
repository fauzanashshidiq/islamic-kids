package com.pam.uas

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.bumptech.glide.Glide
import com.pam.uas.databinding.ActivityDetailKisahNabiBinding

class DetailKisahNabiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailKisahNabiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKisahNabiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- 1. LOGIKA TOMBOL KEMBALI ---
        binding.btnBack.setOnClickListener { view ->
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

        // 2. Ambil Data dari Intent
        val nama = intent.getStringExtra("EXTRA_NAMA") ?: "Nama Nabi"
        val usia = intent.getStringExtra("EXTRA_USIA") ?: "-"
        val tempat = intent.getStringExtra("EXTRA_TMP") ?: "-"
        val tahun = intent.getStringExtra("EXTRA_THN") ?: "-"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "Tidak ada deskripsi."

        // 3. Tampilkan Data ke UI
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
            else -> R.color.white // Warna default (Putih)
        }

        // 1. Ambil warna utama
        val colorMain = ContextCompat.getColor(context, warnaResId)

        // 2. Buat warna shadow (lebih gelap 20%)
        val colorShadow = ColorUtils.blendARGB(colorMain, Color.BLACK, 0.2f)

        // 3. Terapkan ke ID layout yang baru di XML
        binding.layoutHeaderMain.background.setTint(colorMain)
        binding.viewHeaderShadow.background.setTint(colorShadow)
    }
}
