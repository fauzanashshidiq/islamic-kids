package com.pam.uas

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.res.AssetFileDescriptor // Import tambahan
import android.media.MediaPlayer // Import tambahan
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pam.uas.sfx.SfxPlayer
import java.io.IOException // Import tambahan

class Welcome : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var shakeRunnable: Runnable? = null

    private var welcomePlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvTitle = findViewById<View>(R.id.tv_welcome_title)
        val tvSubtitle = findViewById<View>(R.id.tv_welcome_subtitle)
        val imgHero = findViewById<ImageView>(R.id.img_hero_welcome)
        val btnContainer = findViewById<View>(R.id.btn_mulai_container)
        val btnMulai = findViewById<AppCompatButton>(R.id.btn_mulai)

        tvTitle.alpha = 0f
        tvTitle.translationY = -50f
        tvSubtitle.alpha = 0f
        tvSubtitle.translationY = -30f
        imgHero.alpha = 0f
        imgHero.scaleX = 0.8f
        imgHero.scaleY = 0.8f
        btnMulai.alpha = 0f
        btnMulai.translationY = 100f
        btnContainer.alpha = 0f
        btnContainer.translationY = 100f
        playWelcomeVoice()

        imgHero.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                startPulseAnimation(imgHero)
            }
            .start()

        tvTitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(300)
            .setInterpolator(OvershootInterpolator())
            .start()

        tvSubtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(500)
            .setInterpolator(OvershootInterpolator())
            .start()

        btnContainer.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(800)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                startButtonShakeEvery2Seconds(btnContainer)
            }
            .start()

        btnMulai.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(800)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                startButtonShakeEvery2Seconds(btnMulai)
            }
            .start()

        btnMulai.setOnClickListener {
            stopWelcomeVoice()

            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun playWelcomeVoice() {
        try {
            welcomePlayer = MediaPlayer()
            val afd: AssetFileDescriptor = assets.openFd("sfx/voice_welcome.mp3")

            welcomePlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()

            welcomePlayer?.prepare()
            welcomePlayer?.isLooping = false
            welcomePlayer?.setVolume(1.5f, 1.5f)
            welcomePlayer?.start()

            welcomePlayer?.setOnCompletionListener {
                it.release()
                welcomePlayer = null
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopWelcomeVoice() {
        try {
            if (welcomePlayer != null) {
                if (welcomePlayer!!.isPlaying) {
                    welcomePlayer?.stop()
                }
                welcomePlayer?.release()
                welcomePlayer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        stopWelcomeVoice()

        shakeRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopWelcomeVoice()
        shakeRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun startPulseAnimation(view: View) {
        val scalePulse = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", 1.05f),
            PropertyValuesHolder.ofFloat("scaleY", 1.05f)
        )
        scalePulse.duration = 800
        scalePulse.repeatCount = ObjectAnimator.INFINITE
        scalePulse.repeatMode = ObjectAnimator.REVERSE
        scalePulse.start()
    }

    private fun startButtonShakeEvery2Seconds(view: View) {
        shakeRunnable = object : Runnable {
            override fun run() {
                val shake = ObjectAnimator.ofFloat(view, "rotation", 0f, 4f, -4f, 4f, -4f, 0f)
                shake.duration = 500
                shake.start()

                handler.postDelayed(this, 2000)
            }
        }
        handler.post(shakeRunnable!!)
    }
}