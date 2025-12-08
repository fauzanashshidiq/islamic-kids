package com.pam.uas

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.ActivityDoaBinding
import com.pam.uas.DoaAdapter
import com.pam.uas.viewmodel.DoaViewModel

class DoaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoaBinding
    private val viewModel: DoaViewModel by viewModels()
    private lateinit var adapter: DoaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        adapter = DoaAdapter(emptyList())
        binding.rvDoa.layoutManager = LinearLayoutManager(this)
        binding.rvDoa.adapter = adapter

        // Button load API
        binding.btnLoad.setOnClickListener {
            viewModel.loadFromApi()
        }

        // Observe Room data → tampil di UI
        viewModel.listDoa.observe(this) { list ->
            adapter.updateData(list)
        }
    }
}
