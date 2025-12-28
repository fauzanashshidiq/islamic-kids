package com.pam.uas.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.PinActivity
import com.pam.uas.databinding.FragmentMainBinding
import com.pam.uas.ui.DoaMainAdapter
import com.pam.uas.viewmodel.DoaViewModel

class MainFragment : Fragment() {

 private var _binding: FragmentMainBinding? = null
 private val binding get() = _binding!!

 private val viewModel: DoaViewModel by viewModels()
 private lateinit var adapter: DoaMainAdapter

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
 ): View {
  _binding = FragmentMainBinding.inflate(inflater, container, false)
  return binding.root
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)

  // Setup Adapter
  adapter = DoaMainAdapter(emptyList()) { doa, isMemorized ->
   viewModel.updateMemorizedStatus(doa, isMemorized)
  }

  // Setup RecyclerView (Gunakan requireContext() di fragment)
  binding.rvDoaMain.layoutManager = LinearLayoutManager(requireContext())
  binding.rvDoaMain.adapter = adapter

  // Load Data
  viewModel.loadSavedDoa()

  // Observe Data
  // viewLifecycleOwner adalah lifecycle aman untuk Fragment
  viewModel.savedDoa.observe(viewLifecycleOwner) { list ->
   adapter.updateData(list)
   Log.d("MAIN_FRAGMENT", "Data doa ter-load: ${list.size}")
  }
 }

 // Refresh data saat kembali ke fragment ini
 override fun onResume() {
  super.onResume()
  viewModel.loadSavedDoa()
 }

 override fun onDestroyView() {
  super.onDestroyView()
  _binding = null // Mencegah memory leak
 }
}
