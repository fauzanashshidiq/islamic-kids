package com.pam.uas.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.databinding.FragmentKisahNabiBinding
import com.pam.uas.ui.KisahNabiAdapter
import com.pam.uas.viewmodel.KisahNabiViewModel

class KisahNabiFragment : Fragment() {

 private var _binding: FragmentKisahNabiBinding? = null
 private val binding get() = _binding!!

 private val viewModel: KisahNabiViewModel by viewModels()
 private lateinit var adapter: KisahNabiAdapter

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View {
  _binding = FragmentKisahNabiBinding.inflate(inflater, container, false)
  return binding.root
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)

  // Setup Recycler View
  adapter = KisahNabiAdapter(emptyList())

  // Gunakan requireContext() pengganti 'this'
  binding.rvKisahNabi.layoutManager = LinearLayoutManager(requireContext())
  binding.rvKisahNabi.adapter = adapter

  // Panggil fungsi preload
  viewModel.preloadKisahNabi()

  // Observe data menggunakan viewLifecycleOwner (Lifecycle aman untuk Fragment)
  viewModel.kisahNabiList.observe(viewLifecycleOwner) { list ->
   adapter.updateData(list)
  }
 }

 override fun onDestroyView() {
  super.onDestroyView()
  _binding = null // Mencegah memory leak
 }
}
