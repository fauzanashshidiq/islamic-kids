package com.pam.uas

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.ActivityMainBinding
import com.pam.uas.ui.DoaMainAdapter
import com.pam.uas.viewmodel.DoaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DoaViewModel by viewModels()
    private lateinit var adapter: DoaMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DoaMainAdapter(emptyList())
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
