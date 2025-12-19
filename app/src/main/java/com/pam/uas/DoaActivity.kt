package com.pam.uas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.map
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.ActivityDoaBinding
import com.pam.uas.ui.DoaApiCheckboxAdapter
import com.pam.uas.viewmodel.DoaViewModel

class DoaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoaBinding
    private val viewModel: DoaViewModel by viewModels()
    private lateinit var adapter: DoaApiCheckboxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DoaApiCheckboxAdapter(emptyList()) { item, isChecked ->
            if (isChecked) {
                // User mencentang -> Simpan ke DB
                viewModel.saveDoa(item)
            } else {
                // User hapus centang -> Hapus dari DB berdasarkan Judul
                viewModel.deleteDoaByJudul(item.doa)
            }
        }

        binding.rvDoaApi.adapter = adapter
        binding.rvDoaApi.layoutManager = LinearLayoutManager(this)

        viewModel.apiDoaList.observe(this) { list ->
            adapter.updateData(list)
        }

        viewModel.savedDoa.observe(this) { savedList ->
            val savedTitles = savedList.map { it.doa }
            adapter.setSavedIds(savedTitles)
        }

        viewModel.loadApiDoa()
        viewModel.loadSavedDoa()

        binding.btnSimpanKeMenuUtama.text = "Selesai / Kembali"
        binding.btnSimpanKeMenuUtama.setOnClickListener {
            finish()
        }
    }
}
