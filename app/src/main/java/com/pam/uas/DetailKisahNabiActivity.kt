package com.pam.uas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pam.uas.databinding.ActivityDetailKisahNabiBinding

class DetailKisahNabiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailKisahNabiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKisahNabiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Setup Toolbar (Tombol Back)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Kita pakai text custom

        binding.toolbar.setNavigationOnClickListener {
            finish() // Kembali ke fragment sebelumnya
        }

        // 2. Ambil Data dari Intent
        val nama = intent.getStringExtra("EXTRA_NAMA") ?: "Nama Nabi"
        val usia = intent.getStringExtra("EXTRA_USIA") ?: "-"
        val tempat = intent.getStringExtra("EXTRA_TMP") ?: "-"
        val tahun = intent.getStringExtra("EXTRA_THN") ?: "-"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "Tidak ada deskripsi."
        val imageUrl = intent.getStringExtra("EXTRA_IMAGE")

        // 3. Tampilkan Data ke UI
        binding.tvDetailNama.text = nama
        binding.tvUsia.text = "$usia Tahun"
        binding.tvTempat.text = tempat
        binding.tvTahun.text = "$tahun SM"
        binding.tvDeskripsi.text = desc

        // 4. Load Gambar Header
        // Gunakan Glide agar mudah load URL dari internet
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground) // Ganti dengan placeholder kamu
                .error(R.drawable.ic_launcher_foreground)      // Ganti dengan gambar error kamu
                .into(binding.ivDetailNabi)
        }
    }
}
