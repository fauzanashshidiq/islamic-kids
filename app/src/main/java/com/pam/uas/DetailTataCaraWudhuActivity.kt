package com.pam.uas

import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pam.uas.data.local.entity.PembelajaranEntity
import com.pam.uas.databinding.ActivityDetailTataCaraWudhuBinding
import com.pam.uas.sfx.SfxPlayer
import com.pam.uas.viewmodel.PembelajaranViewModel

class DetailTataCaraWudhuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTataCaraWudhuBinding
    private val viewModel: PembelajaranViewModel by viewModels()

    private var materiList: List<PembelajaranEntity> = emptyList()
    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate binding
        binding = ActivityDetailTataCaraWudhuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil Data dari Intent
        val kategoriKey = intent.getStringExtra("EXTRA_KATEGORI") ?: "TATA_CARA_WUDHU"
        val judulKategori = intent.getStringExtra("EXTRA_JUDUL") ?: "Tata Cara Wudhu"

        // Set Header
        binding.tvHeaderKategori.text = judulKategori

        // 2. Load Data dari Database
        viewModel.getMateri(kategoriKey).observe(this) { list ->
            if (list.isNotEmpty()) {
                materiList = list.sortedBy { it.urutan }
                currentIndex = 0
                setupDots()
                tampilkanData()
            } else {
                Toast.makeText(this, "Data materi kosong untuk $kategoriKey", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Setup Tombol Logic dengan Animasi
        binding.btnNext.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            // Animasi Tombol
            view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .setInterpolator(android.view.animation.BounceInterpolator())
                        .start()

                    if (currentIndex < materiList.size - 1) {
                         // Jalankan Animasi Card Transisi
                        animateCardTransition {
                            currentIndex++
                            tampilkanData()
                        }
                    } else {
                        Toast.makeText(this, "Sudah di akhir materi", Toast.LENGTH_SHORT).show()
                    }
                }
                .start()
        }

        binding.btnPrev.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            // Animasi Tombol
            view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .setInterpolator(android.view.animation.BounceInterpolator())
                        .start()

                    if (currentIndex > 0) {
                        // Jalankan Animasi Card Transisi
                        animateCardTransition {
                            currentIndex--
                            tampilkanData()
                        }
                    } else {
                        Toast.makeText(this, "Ini materi pertama", Toast.LENGTH_SHORT).show()
                    }
                }
                .start()
        }

        binding.btnBack.setOnClickListener {
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
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

    /**
     * Animasi Card mengecil lalu membal (bounce) saat konten berubah.
     */
    private fun animateCardTransition(onUpdate: () -> Unit) {
        val durationShrink = 150L
        val durationExpand = 400L
        val interpolator = android.view.animation.BounceInterpolator()

        // Objek yang ingin dianimasikan: Card Utama, Shadow-nya, dan Badge Nomor
        val viewsToAnimate = listOf(binding.cardContent, binding.cardShadow, binding.tvNomorUrut)

        viewsToAnimate.forEach { v ->
            v.animate()
                .scaleX(0.9f) // Kecilkan ke 90%
                .scaleY(0.9f)
                .setDuration(durationShrink)
                .withEndAction {
                    // Hanya panggil update sekali (misal trigger dari cardContent)
                    if (v == binding.cardContent) {
                        onUpdate()
                    }
                    
                    // Kembalikan ke ukuran normal dengan efek bounce
                    v.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(durationExpand)
                        .setInterpolator(interpolator)
                        .start()
                }
                .start()
        }
    }

    private fun setupDots() {
        binding.layoutDots.removeAllViews()
        val dotsCount = materiList.size

        for (i in 0 until dotsCount) {
            val dot = View(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params
            binding.layoutDots.addView(dot)
        }
        updateDots()
    }

    private fun updateDots() {
        val count = binding.layoutDots.childCount
        for (i in 0 until count) {
            val dot = binding.layoutDots.getChildAt(i)
            val params = dot.layoutParams as LinearLayout.LayoutParams

            if (i == currentIndex) {
                // Active Dot: Panjang (Pill)
                params.width = dpToPx(24)
                params.height = dpToPx(8)
                dot.background = ContextCompat.getDrawable(this, R.drawable.bg_pill_beige)
                dot.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFC9A0"))
            } else {
                // Inactive Dot: Bulat Kecil - Gray
                params.width = dpToPx(8)
                params.height = dpToPx(8)
                dot.background = ContextCompat.getDrawable(this, R.drawable.bg_circle_white_solid)
                dot.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BDBDBD"))
            }
            dot.layoutParams = params
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
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

        updateDots()
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
