package com.pam.uas

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pam.uas.databinding.ActivityMainBinding
import com.pam.uas.fragment.DoaFragment
import com.pam.uas.fragment.KisahNabiFragment
import com.pam.uas.fragment.MainFragment
import com.pam.uas.fragment.PembelajaranFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val pinResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadFragment(DoaFragment())
            binding.bottomNavigation.setOnItemSelectedListener(null)
            binding.bottomNavigation.selectedItemId = R.id.nav_doa
            binding.rootLayout.setBackgroundResource(R.drawable.bgsplashscreen)
            updateBottomNavAnimation(binding.bottomNavigation, R.id.nav_doa)
            setupBottomNavListener()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.rootLayout.setBackgroundResource(R.drawable.bgsplashscreen)
            navView.post {
                updateBottomNavAnimation(navView, R.id.nav_main)
            }
        }
        setupBottomNavListener()
    }

    private fun setupBottomNavListener() {
        val navView = binding.bottomNavigation
        navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> {
                    loadFragment(MainFragment())
                    updateBottomNavAnimation(navView, R.id.nav_main)
                    binding.rootLayout.setBackgroundResource(R.drawable.bgsplashscreen)
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
                    binding.rootLayout.setBackgroundResource(R.drawable.bgsplashscreen)
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

            // 2. MATIKAN CLIP PADA CONTAINER ITEM JUGA!
            if (itemView is android.view.ViewGroup) {
                itemView.clipChildren = false
                itemView.clipToPadding = false
            }

            // 3. Ambil Icon (ImageView)
            val iconView = itemView.findViewById<View>(com.google.android.material.R.id.navigation_bar_item_icon_view)

            if (iconView != null) {
                if (viewId == selectedItemId) {
                    // POSISI AKTIF
                    animateView(iconView, -40f, 1.4f) // Naik lebih tinggi (-60f)
                } else {
                    // POSISI TIDAK AKTIF
                    animateView(iconView, 0f, 1.2f)
                }
            }
        }
    }

    private fun animateView(view: View, translationY: Float, scale: Float) {
        // Kita tidak perlu main elevation/translationZ berlebihan jika clipChildren sudah mati total
        view.animate()
            .translationY(translationY)
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(300)
            .start()
    }
}
