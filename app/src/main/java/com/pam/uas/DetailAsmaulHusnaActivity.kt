package com.pam.uas

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pam.uas.data.local.entity.PembelajaranEntity
import com.pam.uas.databinding.ActivityDetailAsmaulHusnaBinding
import com.pam.uas.sfx.SfxPlayer
import com.pam.uas.viewmodel.PembelajaranViewModel

class DetailAsmaulHusnaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAsmaulHusnaBinding
    private val viewModel: PembelajaranViewModel by viewModels()

    private var materiList: List<PembelajaranEntity> = emptyList()
    private var currentIndex = 0
    private var mediaPlayer: MediaPlayer? = null
    private var isMuted = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAsmaulHusnaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kategoriKey = intent.getStringExtra("EXTRA_KATEGORI") ?: "RUKUN_ISLAM"
        val judulKategori = intent.getStringExtra("EXTRA_JUDUL") ?: "Rukun Islam"

        // Set Header
        binding.tvHeaderKategori.text = judulKategori

        viewModel.getMateri(kategoriKey).observe(this) { list ->
            if (list.isNotEmpty()) {
                materiList = list.sortedBy { it.urutan }
                currentIndex = 0
                tampilkanData()
            } else {
                Toast.makeText(this, "Data materi kosong untuk $kategoriKey", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener { view ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
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
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                isMuted = true
                stopVoice()
                updateMuteIcon()
            } else {
                isMuted = false
                updateMuteIcon()

                try {
                    val afd = assets.openFd("music/bg_music2.mp3")
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                        afd.close()
                        prepare()
                        start()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Gagal memutar musik", Toast.LENGTH_SHORT).show()
                }
            }
        }

        updateMuteIcon()
    }

    private fun updateMuteIcon() {
        if (isMuted) {
            binding.btnSuara.setImageResource(R.drawable.ic_mute)
        } else {
            binding.btnSuara.setImageResource(R.drawable.sound)
        }
    }

    private fun animateCardTransition(onUpdate: () -> Unit) {
        val durationShrink = 150L
        val durationExpand = 400L
        val interpolator = android.view.animation.BounceInterpolator()

        val viewsToAnimate = listOf(binding.cardContent, binding.cardShadow, binding.tvNomorUrut)

        viewsToAnimate.forEach { v ->
            v.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(durationShrink)
                .withEndAction {
                    if (v == binding.cardContent) {
                        onUpdate()
                    }

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
    }

    private fun stopVoice() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    override fun onResume() {
        super.onResume()
        if (!isMuted && mediaPlayer != null && !mediaPlayer!!.isPlaying) {
             mediaPlayer?.start()
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
        stopVoice()
    }
}
