package com.pam.uas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.ActivityMainBinding
import com.pam.uas.ui.DoaMainAdapter
import com.pam.uas.viewmodel.DoaViewModel
import com.pam.uas.viewmodel.PembelajaranViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DoaViewModel by viewModels()
    private lateinit var adapter: DoaMainAdapter
    //CUMAN CEK
    private val pembelajaranViewModel: PembelajaranViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CUMAN CEK
        pembelajaranViewModel.preloadPembelajaran()
        pembelajaranViewModel.getMateri("ASMAUL_HUSNA").observe(this) { list ->
            if (list.isNotEmpty()) {
                // Jika log ini muncul, berarti BERHASIL!
                Log.d("CEK_DB", "Berhasil! Ditemukan ${list.size} data Shalat Wajib.")

                // Cek isi data pertama
                Log.d("CEK_DB", "Data pertama: ${list[0].nama}")
            } else {
                // Jika log ini muncul terus, mungkin JSON belum terbaca atau kategori salah
                Log.d("CEK_DB", "Data masih kosong atau sedang loading...")
            }
        }

        adapter = DoaMainAdapter(emptyList()) { doa, isMemorized ->
            viewModel.updateMemorizedStatus(doa, isMemorized)
        }
        binding.rvDoaMain.layoutManager = LinearLayoutManager(this)
        binding.rvDoaMain.adapter = adapter

        binding.btnLihatSemua.setOnClickListener {
            startActivity(Intent(this, PinActivity::class.java))
        }

        viewModel.loadSavedDoa()

        viewModel.savedDoa.observe(this) { list ->
            adapter.updateData(list)
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.loadSavedDoa()
    }
}
