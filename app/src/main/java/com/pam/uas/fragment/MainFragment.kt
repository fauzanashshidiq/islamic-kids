package com.pam.uas.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pam.uas.R // Pastikan import R ada
import com.pam.uas.databinding.FragmentMainBinding
import com.pam.uas.ui.DoaMainAdapter
import com.pam.uas.viewmodel.DoaViewModel

class MainFragment : Fragment() {

 private var _binding: FragmentMainBinding? = null
 private val binding get() = _binding!!

 private val viewModel: DoaViewModel by viewModels()
 private lateinit var adapter: DoaMainAdapter

 // Variabel untuk menyimpan mode filter saat ini (Default: ALL)
 private var currentFilterMode = DoaViewModel.FilterMode.ALL

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

   // LOGIKA EMPTY STATE DINAMIS
   if (list.isEmpty()) {
    binding.rvDoaMain.visibility = View.GONE
    binding.layoutEmptyState.visibility = View.VISIBLE

    // Panggil fungsi update teks kosong
    updateEmptyStateText()
   } else {
    binding.rvDoaMain.visibility = View.VISIBLE
    binding.layoutEmptyState.visibility = View.GONE
   }
  }
 }

 // Fungsi baru untuk mengubah teks berdasarkan filter
 private fun updateEmptyStateText() {
  // Ambil referensi TextView dari dalam layoutEmptyState (karena di XML pakai LinearLayout biasa)
  // Cara akses anak view dari binding layoutEmptyState:
  val layoutEmpty = binding.layoutEmptyState
  val ivIcon = layoutEmpty.getChildAt(0) as ImageView // Index 0 = Gambar
  val tvJudul = layoutEmpty.getChildAt(1) as TextView // Index 1 adalah Judul (lihat urutan XML)
  val tvDeskripsi = layoutEmpty.getChildAt(2) as TextView // Index 2 adalah Deskripsi

  when (currentFilterMode) {
   DoaViewModel.FilterMode.ALL -> {
    ivIcon.setImageResource(R.drawable.ic_search_preview)
    tvJudul.text = "Wah, List Doanya Masih Kosong!"
    tvDeskripsi.text = "Yuk, minta Ayah atau Bunda untuk tambah doa baru di sini. Semangat ya!"
   }
   DoaViewModel.FilterMode.MEMORIZED -> {
    ivIcon.setImageResource(R.drawable.ic_launcher_foreground)
    tvJudul.text = "Siap Jadi Anak Hebat?"
    tvDeskripsi.text = "Pilih satu doa dan hapalkan pelan-pelan ya. Kamu pasti bisa!"
   }
   DoaViewModel.FilterMode.NOT_MEMORIZED -> {
    ivIcon.setImageResource(R.drawable.ic_launcher_foreground)
    tvJudul.text = "Masya Allah, Kamu Hebat!"
    tvDeskripsi.text = "Minta Ayah atau Bunda tambah doa baru lagi yuk!"
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
     binding.chipSemua.id -> {
      currentFilterMode = DoaViewModel.FilterMode.ALL // Update mode
      viewModel.setFilter(DoaViewModel.FilterMode.ALL)
     }
     binding.chipSudahHapal.id -> {
      currentFilterMode = DoaViewModel.FilterMode.MEMORIZED // Update mode
      viewModel.setFilter(DoaViewModel.FilterMode.MEMORIZED)
     }
     binding.chipBelumHapal.id -> {
      currentFilterMode = DoaViewModel.FilterMode.NOT_MEMORIZED // Update mode
      viewModel.setFilter(DoaViewModel.FilterMode.NOT_MEMORIZED)
     }
    }
   }
  }
 }

 override fun onResume() {
  super.onResume()
  viewModel.loadSavedDoa()
 }

 override fun onDestroyView() {
  super.onDestroyView()
  _binding = null
 }
}
