package com.pam.uas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

            // Kita matikan listener sebentar supaya baris di bawah ini
            // tidak memicu event klik 'nav_doa' lagi.
            binding.bottomNavigation.setOnItemSelectedListener(null)

            binding.bottomNavigation.selectedItemId = R.id.nav_doa

            // Nyalakan listener lagi
            setupBottomNavListener()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tampilkan MainFragment saat aplikasi pertama kali dibuka
        if (savedInstanceState == null) {
            loadFragment(MainFragment())
        }

        setupBottomNavListener()
    }

    private fun setupBottomNavListener() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> {
                    loadFragment(MainFragment())
                    true
                }
                R.id.nav_doa -> {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

                    // Jika user sudah ada di halaman Doa, tidak perlu PIN lagi
                    if (currentFragment is DoaFragment) {
                        return@setOnItemSelectedListener true
                    }

                    // Luncurkan PIN
                    val intent = Intent(this, PinActivity::class.java)
                    pinResultLauncher.launch(intent)

                    // Return false: Jangan ganti icon jadi aktif dulu (tunggu hasil PIN)
                    false
                }
                R.id.nav_kisah -> {
                    loadFragment(KisahNabiFragment())
                    true
                }
                R.id.nav_pembelajaran -> {
                    loadFragment(PembelajaranFragment())
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
}
