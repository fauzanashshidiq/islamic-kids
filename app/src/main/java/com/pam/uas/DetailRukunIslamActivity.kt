package com.pam.uas

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pam.uas.data.local.entity.PembelajaranEntity
import com.pam.uas.databinding.ActivityDetailRukunIslamBinding
import com.pam.uas.sfx.SfxPlayer
import com.pam.uas.viewmodel.PembelajaranViewModel

class DetailRukunIslamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRukunIslamBinding
    private val viewModel: PembelajaranViewModel by viewModels()

    private var materiList: List<PembelajaranEntity> = emptyList()
    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    // Untuk update progress bar suara
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRukunIslamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Header Title
        val kategoriKey = intent.getStringExtra("EXTRA_KATEGORI") ?: "RUKUN_ISLAM"
        val judulKategori = intent.getStringExtra("EXTRA_JUDUL") ?: "Rukun Islam"
        binding.tvHeaderKategori.text = judulKategori

        // Load Data
        viewModel.getMateri(kategoriKey).observe(this) { list ->
            if (list.isNotEmpty()) {
                materiList = list.sortedBy { it.urutan }
                currentIndex = 0
                setupDots()
                tampilkanData()
            } else {
                Toast.makeText(this, "Data materi kosong", Toast.LENGTH_SHORT).show()
            }
        }

        // --- SETUP TOMBOL NEXT/PREV (Sama, pakai animateButton) ---
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

        // --- SETUP TOMBOL BACK (Dengan Animasi) ---
        binding.btnBack.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            animateButton(view) {
                finish()
            }
        }

        // --- SETUP TOMBOL SUARA (Dengan Progress & Animasi) ---
        binding.btnSuara.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            animateButton(view) {
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
                    .setInterpolator(OvershootInterpolator(2f))
                    .withEndAction {
                        onEndAction()
                    }
                    .start()
            }
            .start()
    }

    private fun animateCardTransition(onUpdate: () -> Unit) {
        val durationShrink = 150L
        val durationExpand = 400L
        val interpolator = android.view.animation.BounceInterpolator()
        val viewsToAnimate = listOf(binding.cardContent, binding.cardShadow, binding.tvNomorUrut)

        viewsToAnimate.forEach { v ->
            v.animate()
                .scaleX(0.9f).scaleY(0.9f)
                .setDuration(durationShrink)
                .withEndAction {
                    if (v == binding.cardContent) onUpdate()
                    v.animate().scaleX(1.0f).scaleY(1.0f)
                        .setDuration(durationExpand).setInterpolator(interpolator).start()
                }
                .start()
        }
    }

    // --- SETUP DOTS INDICATOR ---
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
                // Active Dot: Panjang (Pill) - Purple #D900FF
                params.width = dpToPx(24)
                params.height = dpToPx(8)
                dot.background = ContextCompat.getDrawable(this, R.drawable.bg_header_blue_gradient)
                dot.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#29B6F6"))
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

        binding.tvNomorUrut.text = item.urutan.toString()
        binding.tvJudulMateri.text = item.nama
        binding.tvDeskripsiMateri.text = item.deskripsi

        // Image
        if (!item.image_path.isNullOrEmpty()) {
            val resId = resources.getIdentifier(item.image_path, "drawable", packageName)
            if (resId != 0) binding.ivMateri.setImageResource(resId)
            else binding.ivMateri.setImageResource(R.drawable.ic_launcher_foreground)
        } else {
             binding.ivMateri.setImageResource(R.drawable.ic_launcher_foreground)
        }

        // Handle Arab
        if (item.teks_arab.isNullOrEmpty()) {
            binding.tvArabMateri.visibility = View.GONE
        } else {
            binding.tvArabMateri.visibility = View.VISIBLE
            binding.tvArabMateri.text = item.teks_arab
        }

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
            startProgressUpdater()
            mediaPlayer?.setOnCompletionListener {
                stopVoice()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal memutar suara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startProgressUpdater() {
        binding.progressSuara.progress = 0
        binding.progressSuara.max = mediaPlayer?.duration ?: 100

        progressRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        val currentPosition = player.currentPosition
                        binding.progressSuara.progress = currentPosition
                        handler.postDelayed(this, 50)
                    }
                }
            }
        }
        handler.post(progressRunnable!!)
    }

    private fun stopVoice() {
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressRunnable = null
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
