package com.pam.uas

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pam.uas.data.local.entity.PembelajaranEntity
import com.pam.uas.databinding.ActivityDetailTataCaraWudhuBinding // PENTING: Gunakan binding yang baru
import com.pam.uas.viewmodel.PembelajaranViewModel

class DetailTataCaraWudhuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTataCaraWudhuBinding
    private val viewModel: PembelajaranViewModel by viewModels()

    private var materiList: List<PembelajaranEntity> = emptyList()
    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate binding Rukun Islam
        binding = ActivityDetailTataCaraWudhuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil Data dari Intent (Dikirim dari PembelajaranFragment)
        // Defaultnya kita set "RUKUN_ISLAM" jika intent kosong, sebagai jaga-jaga
        val kategoriKey = intent.getStringExtra("EXTRA_KATEGORI") ?: "RUKUN_ISLAM"
        val judulKategori = intent.getStringExtra("EXTRA_JUDUL") ?: "Rukun Islam"

        // Set Header
        binding.tvHeaderKategori.text = judulKategori

        // 2. Load Data dari Database
        viewModel.getMateri(kategoriKey).observe(this) { list ->
            if (list.isNotEmpty()) {
                materiList = list.sortedBy { it.urutan }
                currentIndex = 0
                tampilkanData()
            } else {
                Toast.makeText(this, "Data materi kosong untuk $kategoriKey", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Setup Tombol Logic (Sama persis)
        binding.btnNext.setOnClickListener {
            if (currentIndex < materiList.size - 1) {
                currentIndex++
                tampilkanData()
            } else {
                Toast.makeText(this, "Sudah di akhir materi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                tampilkanData()
            } else {
                Toast.makeText(this, "Ini materi pertama", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSuara.setOnClickListener {
            val item = materiList.getOrNull(currentIndex)
            item?.let {
                if (!it.voice_path.isNullOrEmpty()) {
                    playVoice(it.voice_path)
                } else {
                    Toast.makeText(this, "Suara tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun tampilkanData() {
        if (materiList.isEmpty()) return
        val item = materiList[currentIndex]

        // Binding View
        binding.tvNomorUrut.text = item.urutan.toString()
        binding.tvJudulMateri.text = item.nama
        binding.tvDeskripsiMateri.text = item.deskripsi
        binding.tvKeterangan.text = item.keterangan

        // Handle Arab
        if (item.teks_arab.isNullOrEmpty()) {
            binding.tvArabMateri.visibility = View.GONE
        } else {
            binding.tvArabMateri.visibility = View.VISIBLE
            binding.tvArabMateri.text = item.teks_arab
        }

        // Handle Gambar
        if (!item.image_path.isNullOrEmpty()) {
            val resId = resources.getIdentifier(
                item.image_path, "drawable", packageName
            )
            if (resId != 0) {
                binding.ivMateri.setImageResource(resId)
            } else {
                binding.ivMateri.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }

        // Handle Visibility Button
        binding.btnPrev.visibility = if (currentIndex == 0) View.INVISIBLE else View.VISIBLE
        binding.btnNext.visibility = if (currentIndex == materiList.size - 1) View.INVISIBLE else View.VISIBLE

        stopVoice()
    }

    private fun playVoice(fileName: String) {
        stopVoice()
        try {
            val finalName = if (fileName.endsWith(".mp3")) fileName else "$fileName.mp3"
            val descriptor = assets.openFd(finalName)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                descriptor.close()
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal memutar: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopVoice() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVoice()
    }
}
