package com.pam.uas.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.DetailKisahNabiActivity
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
  adapter = KisahNabiAdapter(emptyList()) { nabi ->
   val intent = Intent(requireContext(), DetailKisahNabiActivity::class.java)

   intent.putExtra("EXTRA_NAMA", nabi.name)
   intent.putExtra("EXTRA_USIA", nabi.usia)
   intent.putExtra("EXTRA_TMP", nabi.tmp)
   intent.putExtra("EXTRA_THN", nabi.thnKelahiran)
   intent.putExtra("EXTRA_DESC", nabi.description)

   startActivity(intent)
  }

  binding.rvKisahNabi.layoutManager = LinearLayoutManager(requireContext())
  binding.rvKisahNabi.adapter = adapter

  // Panggil fungsi preload
  viewModel.preloadKisahNabi()

  viewModel.kisahNabiList.observe(viewLifecycleOwner) { list ->
   adapter.updateData(list)
  }
 }

 override fun onDestroyView() {
  super.onDestroyView()
  _binding = null
 }
}
