package com.pam.uas

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pam.uas.data.local.entity.PembelajaranEntity
import com.pam.uas.databinding.ActivityDetailSifatWajibAllahBinding
import com.pam.uas.sfx.SfxPlayer
import com.pam.uas.viewmodel.PembelajaranViewModel

class DetailSifatWajibAllahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSifatWajibAllahBinding
    private val viewModel: PembelajaranViewModel by viewModels()

    private var materiList: List<PembelajaranEntity> = emptyList()
    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    // Handler untuk update progress bar suara
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate binding
        binding = ActivityDetailSifatWajibAllahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil Data dari Intent
        val kategoriKey = intent.getStringExtra("EXTRA_KATEGORI") ?: "SIFAT_WAJIB_ALLAH"
        val judulKategori = intent.getStringExtra("EXTRA_JUDUL") ?: "Sifat Wajib Allah"

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

        // 3. Setup Tombol Logic dengan Animasi

        // Tombol NEXT
        binding.btnNext.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            animateButton(view) {
                if (currentIndex < materiList.size - 1) {
                    animateCardTransition {
                        currentIndex++
                        tampilkanData()
                    }
                } else {
                    Toast.makeText(this, "Sudah di akhir materi", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Tombol PREV
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

        // Tombol BACK
        binding.btnBack.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            animateButton(view) {
                finish()
            }
        }

        // Tombol SUARA
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
     * Helper Function untuk animasi tombol klik (Scale + Bounce)
     */
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

        // Handle Visibility Button
        binding.btnPrev.visibility = if (currentIndex == 0) View.INVISIBLE else View.VISIBLE
        binding.btnNext.visibility = if (currentIndex == materiList.size - 1) View.INVISIBLE else View.VISIBLE

        stopVoice()
    }

    // --- LOGIC SUARA BARU ---

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
