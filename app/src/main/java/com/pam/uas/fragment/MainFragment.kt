package com.pam.uas.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.FragmentMainBinding
import com.pam.uas.ui.DoaMainAdapter
import com.pam.uas.viewmodel.DoaViewModel

class MainFragment : Fragment() {

 private var _binding: FragmentMainBinding? = null
 private val binding get() = _binding!!

 private val viewModel: DoaViewModel by viewModels()
 private lateinit var adapter: DoaMainAdapter

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View {
  _binding = FragmentMainBinding.inflate(inflater, container, false)
  return binding.root
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)

  setupRecyclerView()
  setupFilterChips()

  // Load data awal
  viewModel.loadSavedDoa()

  // Observe data hasil filter
  viewModel.displayDoaList.observe(viewLifecycleOwner) { list ->
   adapter.updateData(list)

   // Opsional: Tampilkan view kosong jika list kosong
   if (list.isEmpty()) {
    // binding.tvEmptyState.visibility = View.VISIBLE
   } else {
    // binding.tvEmptyState.visibility = View.GONE
   }
  }
 }

 private fun setupRecyclerView() {
  adapter = DoaMainAdapter(emptyList()) { doa, isMemorized ->
   viewModel.updateMemorizedStatus(doa, isMemorized)
  }
  binding.rvDoaMain.layoutManager = LinearLayoutManager(requireContext())
  binding.rvDoaMain.adapter = adapter
 }

 private fun setupFilterChips() {
  binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
   if (checkedIds.isNotEmpty()) {
    when (checkedIds[0]) {
     binding.chipSemua.id -> viewModel.setFilter(DoaViewModel.FilterMode.ALL)
     binding.chipSudahHapal.id -> viewModel.setFilter(DoaViewModel.FilterMode.MEMORIZED)
     binding.chipBelumHapal.id -> viewModel.setFilter(DoaViewModel.FilterMode.NOT_MEMORIZED)
    }
   }
  }
 }

 override fun onResume() {
  super.onResume()
  // Reload data saat kembali ke layar ini (jaga-jaga ada update dari fragment lain)
  viewModel.loadSavedDoa()
 }

 override fun onDestroyView() {
  super.onDestroyView()
  _binding = null
 }
}
