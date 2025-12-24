package com.pam.uas

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.ActivityKisahNabiBinding
import com.pam.uas.ui.KisahNabiAdapter
import com.pam.uas.viewmodel.KisahNabiViewModel

class KisahNabiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKisahNabiBinding
    private val viewModel: KisahNabiViewModel by viewModels()
    private lateinit var adapter: KisahNabiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKisahNabiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Recycler View
        adapter = KisahNabiAdapter(emptyList())
        binding.rvKisahNabi.layoutManager = LinearLayoutManager(this)
        binding.rvKisahNabi.adapter = adapter

        // Panggil fungsi preload (Cek JSON -> Masukkan ke DB jika kosong)
        viewModel.preloadKisahNabi()

        // Observe data dari Database
        viewModel.kisahNabiList.observe(this) { list ->
            adapter.updateData(list)
        }
    }
}