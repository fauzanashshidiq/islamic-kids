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
        navView.outlineProvider = null // Matikan shadow bawaan yang bisa memotong

        // --- TAMBAHAN PENTING ---
        // Cari "BottomNavigationMenuView" (anak langsung dari BottomNavigationView)
        // dan matikan clipChildren-nya agar icon bisa keluar bebas.
        val menuView = navView.getChildAt(0) as? android.view.ViewGroup
        menuView?.clipChildren = false
        menuView?.clipToPadding = false
        // ------------------------

        if (savedInstanceState == null) {
            loadFragment(MainFragment())
            binding.rootLayout.setBackgroundResource(R.drawable.bg_home)
            navView.post {
                updateBottomNavAnimation(navView, R.id.nav_main)
            }
        }
        setupBottomNavListener()
    }

    // --- FUNGSI SETUP MUSIK DARI ASSETS ---
    private fun setupBackgroundMusic() {
        try {
            // Inisialisasi MediaPlayer kosong
            mediaPlayer = MediaPlayer()

            // Buka file dari folder 'assets'
            val afd: AssetFileDescriptor = assets.openFd("music/bg_music.mp3")

            // Set Data Source
            // Penting: Gunakan startOffset dan length agar tidak error
            mediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

            // Tutup file descriptor karena sudah di-set ke player
            afd.close()

            // Persiapan player
            mediaPlayer?.prepare()
            mediaPlayer?.isLooping = true // Biar ngulang terus
            mediaPlayer?.setVolume(0.5f, 0.5f) // Volume 50%
            mediaPlayer?.start() // Mulai mainkan

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // --- LIFECYCLE ---

    override fun onResume() {
        super.onResume()
        // Lanjut mainkan musik jika aplikasi dibuka kembali
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        // Pause musik jika pindah aplikasi / tekan home
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hapus player dari memori saat aplikasi dimatikan total
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

            // 1. Ambil Container Item (BottomNavigationItemView)
            val itemView = navView.findViewById<View>(viewId)

            // 2. Matikan Clip agar bisa keluar jalur
            if (itemView is android.view.ViewGroup) {
                itemView.clipChildren = false
                itemView.clipToPadding = false
            }

            // 3. Ambil Icon di dalamnya
            val iconView = itemView.findViewById<View>(com.google.android.material.R.id.navigation_bar_item_icon_view)

            if (viewId == selectedItemId) {
                // --- KONDISI AKTIF ---

                // A. Beri Background Putih pada Container
                itemView.setBackgroundResource(R.drawable.bg_nav_item_selected)

                // B. Naikkan CONTAINER-nya (ItemView), maka Icon otomatis ikut naik
                // Kita naikkan -50f (sesuaikan tinggi 'pop' yang dimau)
                animateView(itemView, -50f, 1.0f)

                // C. Besarkan Icon-nya sedikit biar manis
                if (iconView != null) {
                    // Icon tidak perlu ditranslate lagi karena containernya sudah naik
                    // Cukup di-scale saja
                    iconView.animate()
                        .scaleX(1.3f)
                        .scaleY(1.3f)
                        .setDuration(300)
                        .start()
                }

            } else {
                // --- KONDISI TIDAK AKTIF ---

                // A. Hapus Background
                itemView.background = null

                // B. Kembalikan Posisi Container ke 0 (Bawah)
                animateView(itemView, 0f, 1.0f)

                // C. Kembalikan Ukuran Icon Normal
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

    // Update helper function biar lebih fleksibel
    private fun animateView(view: View, translationY: Float, scale: Float) {
        view.animate()
            .translationY(translationY)
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(300)
            .setInterpolator(android.view.animation.OvershootInterpolator()) // Efek membal sedikit saat naik
            .start()
    }
}
