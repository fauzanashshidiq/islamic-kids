package com.pam.uas

import android.app.Activity
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pam.uas.databinding.ActivityMainBinding
import com.pam.uas.fragment.DoaFragment
import com.pam.uas.fragment.KisahNabiFragment
import com.pam.uas.fragment.MainFragment
import com.pam.uas.fragment.PembelajaranFragment
import com.pam.uas.sfx.SfxPlayer
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mediaPlayer: MediaPlayer? = null

    private val pinResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadFragment(DoaFragment())
            binding.bottomNavigation.setOnItemSelectedListener(null)
            binding.bottomNavigation.selectedItemId = R.id.nav_doa
            binding.rootLayout.setBackgroundResource(R.drawable.bg_list_doa)
            updateBottomNavAnimation(binding.bottomNavigation, R.id.nav_doa)
            setupBottomNavListener()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackgroundMusic()

        val navView = binding.bottomNavigation
        navView.itemIconTintList = null
        navView.outlineProvider = null

        val menuView = navView.getChildAt(0) as? android.view.ViewGroup
        menuView?.clipChildren = false
        menuView?.clipToPadding = false

        if (savedInstanceState == null) {
            loadFragment(MainFragment())
            binding.rootLayout.setBackgroundResource(R.drawable.bg_home)
            navView.post {
                updateBottomNavAnimation(navView, R.id.nav_main)
            }
        }
        setupBottomNavListener()
    }

    private fun setupBackgroundMusic() {
        try {
            mediaPlayer = MediaPlayer()

            val afd: AssetFileDescriptor = assets.openFd("music/bg_music.mp3")

            mediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

            afd.close()

            mediaPlayer?.prepare()
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(0.5f, 0.5f)
            mediaPlayer?.start()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
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
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun setupBottomNavListener() {
        val navView = binding.bottomNavigation
        navView.setOnItemSelectedListener { menuItem ->
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)
            when (menuItem.itemId) {
                R.id.nav_main -> {

                    loadFragment(MainFragment())
                    updateBottomNavAnimation(navView, R.id.nav_main)
                    binding.rootLayout.setBackgroundResource(R.drawable.bg_home)
                    true
                }
                R.id.nav_doa -> {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (currentFragment is DoaFragment) return@setOnItemSelectedListener true
                    val intent = Intent(this, PinActivity::class.java)
                    pinResultLauncher.launch(intent)
                    false
                }
                R.id.nav_kisah -> {
                    loadFragment(KisahNabiFragment())
                    updateBottomNavAnimation(navView, R.id.nav_kisah)
                    binding.rootLayout.setBackgroundResource(R.drawable.bg_kisah_nabi)
                    true
                }
                R.id.nav_pembelajaran -> {
                    loadFragment(PembelajaranFragment())
                    updateBottomNavAnimation(navView, R.id.nav_pembelajaran)
                    binding.rootLayout.setBackgroundResource(R.drawable.bgpembelajaran)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun updateBottomNavAnimation(navView: BottomNavigationView, selectedItemId: Int) {
        val menu = navView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val viewId = menuItem.itemId

            val itemView = navView.findViewById<View>(viewId)

            if (itemView is android.view.ViewGroup) {
                itemView.clipChildren = false
                itemView.clipToPadding = false
            }

            val iconView = itemView.findViewById<View>(com.google.android.material.R.id.navigation_bar_item_icon_view)

            if (viewId == selectedItemId) {
                itemView.setBackgroundResource(R.drawable.bg_nav_item_selected)

                animateView(itemView, -50f, 1.0f)

                if (iconView != null) {
                    iconView.animate()
                        .scaleX(1.3f)
                        .scaleY(1.3f)
                        .setDuration(300)
                        .start()
                }

            } else {
                itemView.background = null

                animateView(itemView, 0f, 1.0f)

                if (iconView != null) {
                    iconView.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .start()
                }
            }
        }
    }

    private fun animateView(view: View, translationY: Float, scale: Float) {
        view.animate()
            .translationY(translationY)
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(300)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()
    }
}
