package com.pam.uas.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.FragmentDoaBinding
import com.pam.uas.ui.DoaApiCheckboxAdapter
import com.pam.uas.viewmodel.DoaViewModel

class DoaFragment : Fragment() {

    private var _binding: FragmentDoaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DoaViewModel by viewModels()
    private lateinit var adapter: DoaApiCheckboxAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DoaApiCheckboxAdapter(
            list = emptyList(),
            onCheckChanged = { item, isChecked ->
                if (isChecked) {
                    viewModel.saveDoa(item, "")
                } else {
                    viewModel.deleteDoaByJudul(item.doa)
                }
            },
            onSaveNote = { item, newNote ->
                viewModel.updateCatatan(item.doa, newNote)
            }
        )

        binding.rvDoaApi.adapter = adapter
        binding.rvDoaApi.layoutManager = LinearLayoutManager(requireContext()) // Use requireContext()

        // Observer API List
        viewModel.apiDoaList.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
        }

        // Observer Saved Doa
        viewModel.savedDoa.observe(viewLifecycleOwner) { savedList ->
            val notesMap = savedList.associate { it.doa to (it.catatan ?: "") }
            adapter.setSavedData(notesMap)
        }

        // Load Data
        viewModel.loadApiDoa()
        viewModel.loadSavedDoa()

        // Tombol Selesai (Navigasi balik ke home/main)
        binding.btnSimpanKeMenuUtama.text = "Selesai"
        binding.btnSimpanKeMenuUtama.setOnClickListener {
            // Logic opsional: misal pindah tab ke Home secara manual
            // (activity as? MainActivity)?.moveToHome()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
