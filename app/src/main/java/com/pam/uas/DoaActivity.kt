package com.pam.uas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        adapter = DoaApiCheckboxAdapter()
        binding.rvDoaApi.layoutManager = LinearLayoutManager(this)
        binding.rvDoaApi.adapter = adapter

        viewModel.apiDoaList.observe(this) { list ->
            adapter.updateData(list)
        }

        viewModel.loadApiDoa()

        binding.btnSimpanKeMenuUtama.setOnClickListener {
            val selected = adapter.getSelectedItems()
            if (selected.isEmpty()) {
                Toast.makeText(this, "Pilih doa dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveSelectedDoa(selected)
            Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
