package com.pam.uas

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pam.uas.sfx.SfxPlayer

class SplahScreen : AppCompatActivity() {
    // Simpan handler dan runnable sebagai variabel agar bisa dibatalkan
    private val handler = Handler(Looper.getMainLooper())
    private var navigateRunnable: Runnable? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splah_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgLogo = findViewById<ImageView>(R.id.img_hero)
        val tvTitle = findViewById<View>(R.id.tv_title)
        val tvSubtitle = findViewById<View>(R.id.tv_subtitle)

        // Setup Awal
        imgLogo.scaleX = 0.5f
        imgLogo.scaleY = 0.5f
        tvTitle.alpha = 0f
        tvTitle.translationY = 100f
        tvSubtitle.alpha = 0f
        tvSubtitle.translationY = 100f

        // ANIMASI
        imgLogo.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                startPulseAnimation(imgLogo)
            }
            .start()

        tvTitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(300)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                shakeView(tvTitle)
            }
            .start()

        tvSubtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(600)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                shakeView(tvSubtitle)
            }
            .start()

        navigateRunnable = Runnable {
            if (!isFinishing && !isDestroyed) {
                val intent = Intent(this, Welcome::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                SfxPlayer.play(this, SfxPlayer.SoundType.CUTE)
                overridePendingTransition(R.anim.rotate_grow, R.anim.stay_still)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navigateRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onPause() {
        super.onPause()
        navigateRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onResume() {
        super.onResume()
        navigateRunnable?.let { handler.postDelayed(it, 3000) }
    }

    private fun shakeView(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "rotation", 0f, -3f, 3f, -3f, 0f)
        animator.duration = 400
        animator.start()
    }

    private fun startPulseAnimation(view: View) {
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", 1.1f),
            PropertyValuesHolder.ofFloat("scaleY", 1.1f)
        )
        scaleDown.duration = 800
        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE
        scaleDown.start()
    }
}