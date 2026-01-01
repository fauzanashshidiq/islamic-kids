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
import com.pam.uas.databinding.ActivityDetailRukunImanBinding
import com.pam.uas.sfx.SfxPlayer
import com.pam.uas.viewmodel.PembelajaranViewModel
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator


class DetailRukunImanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRukunImanBinding
    private val viewModel: PembelajaranViewModel by viewModels()

    private var materiList: List<PembelajaranEntity> = emptyList()
    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate binding Rukun Iman
        binding = ActivityDetailRukunImanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil Data dari Intent
        val kategoriKey = intent.getStringExtra("EXTRA_KATEGORI") ?: "RUKUN_IMAN"
        val judulKategori = intent.getStringExtra("EXTRA_JUDUL") ?: "Rukun Iman"

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
            animateButton(view) {
                if (currentIndex > 0) {
                    animateCardTransition {
                        currentIndex--
                        tampilkanData()
                    }
                } else {
                    Toast.makeText(this, "Ini materi pertama", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // UBAH LISTENER BUTTON BACK
        binding.btnBack.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            animateButton(view) {
                finish()
            }
        }

        // UBAH LISTENER BUTTON SUARA
        binding.btnSuara.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            animateButton(view) {
                // Logic Play/Stop Toggle
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    stopVoice()
                } else {
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
                dot.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F57C00"))
            } else {
                // Inactive Dot: Bulat Kecil
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
        
        // LOGIC PENGGALAN BARIS (New Line)
        // Menambahkan \n secara dinamis untuk format yang lebih rapi
        var formattedJudul = item.nama
        if (formattedJudul.contains("Iman Kepada", ignoreCase = true) && !formattedJudul.contains("\n")) {
            formattedJudul = formattedJudul.replace("Iman Kepada", "Iman Kepada\n", ignoreCase = true)
        } else if (formattedJudul.contains("Iman kepada", ignoreCase = true) && !formattedJudul.contains("\n")) {
             formattedJudul = formattedJudul.replace("Iman kepada", "Iman Kepada\n", ignoreCase = true)
        }
        
        binding.tvJudulMateri.text = formattedJudul
        binding.tvDeskripsiMateri.text = item.deskripsi
        
        // Keterangan tidak ditampilkan di tampilan card simple seperti referensi, 
        // tapi jika perlu bisa di-set:
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
                // Fallback image jika tidak ditemukan
                 binding.ivMateri.setImageResource(R.drawable.ic_iman_malaikat)
            }
        }

        // Handle Visibility Button Navigasi
        binding.btnPrev.visibility = if (currentIndex == 0) View.INVISIBLE else View.VISIBLE
        binding.btnNext.visibility = if (currentIndex == materiList.size - 1) View.INVISIBLE else View.VISIBLE

        updateDots()
        stopVoice()
    }

    private fun animateButton(view: View, onEndAction: () -> Unit) {
        view.animate()
            .scaleX(0.85f)
            .scaleY(0.85f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(300)
                    .setInterpolator(OvershootInterpolator(2f)) // Efek membal
                    .withEndAction {
                        onEndAction()
                    }
                    .start()
            }
            .start()
    }

    // --- LOGIC SUARA BARU (Copy Paste Gantikan playVoice/stopVoice yang lama) ---

    private fun playVoice(fileName: String) {
        stopVoice() // Reset suara sebelumnya

        try {
            val finalName = if (fileName.endsWith(".mp3")) fileName else "$fileName.mp3"
            val descriptor = assets.openFd(finalName)

            mediaPlayer = MediaPlayer().apply {
                setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                descriptor.close()
                prepare()
                start()
            }

            // Jalankan updater progress bar
            startProgressUpdater()

            mediaPlayer?.setOnCompletionListener {
                stopVoice()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal memutar: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startProgressUpdater() {
        // Reset progress bar
        binding.progressSuara.progress = 0
        binding.progressSuara.max = mediaPlayer?.duration ?: 100

        progressRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        // Update UI
                        binding.progressSuara.progress = player.currentPosition
                        // Ulangi setiap 50ms
                        handler.postDelayed(this, 50)
                    }
                }
            }
        }
        handler.post(progressRunnable!!)
    }

    private fun stopVoice() {
        // Matikan updater
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressRunnable = null

        // Reset Progress Bar ke 0
        binding.progressSuara.progress = 0

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
