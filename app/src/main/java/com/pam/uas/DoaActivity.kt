package com.pam.uas

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.data.remote.response.ApiDoaResponse
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

        adapter = DoaApiCheckboxAdapter(
            list = emptyList(),
            // Callback 1: Checkbox diklik
            onCheckChanged = { item, isChecked ->
                if (isChecked) {
                    // Simpan doa (Catatan default kosong "")
                    viewModel.saveDoa(item, "")
                } else {
                    // Hapus doa
                    viewModel.deleteDoaByJudul(item.doa)
                }
            },
            // Callback 2: Tombol Simpan Catatan diklik
            onSaveNote = { item, newNote ->
                // Update catatan di DB
                viewModel.updateCatatan(item.doa, newNote)
            }
        )

        binding.rvDoaApi.adapter = adapter
        binding.rvDoaApi.layoutManager = LinearLayoutManager(this)

        viewModel.apiDoaList.observe(this) { list ->
            adapter.updateData(list)
        }

        viewModel.savedDoa.observe(this) { savedList ->
            // KITA BUAT MAP: Kuncinya Judul, Valuenya Catatan
            // Supaya adapter tahu doa mana yg disimpan DAN apa catatannya
            val notesMap = savedList.associate { it.doa to (it.catatan ?: "") }
            adapter.setSavedData(notesMap)
        }

        viewModel.loadApiDoa()
        viewModel.loadSavedDoa()

        binding.btnSimpanKeMenuUtama.text = "Selesai / Kembali"
        binding.btnSimpanKeMenuUtama.setOnClickListener {
            finish()
        }
    }
}
