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
import com.pam.uas.R
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
  setupFilterButtons()

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
    ivIcon.setImageResource(R.drawable.ic_semua_doa)
    tvJudul.text = "Wah, List Doanya Masih Kosong!"
    tvDeskripsi.text = "Yuk, minta Ayah atau Bunda untuk tambah doa baru di sini. Semangat ya!"
   }
   DoaViewModel.FilterMode.MEMORIZED -> {
    ivIcon.setImageResource(R.drawable.ic_belum_hapal)
    tvJudul.text = "Siap Jadi Anak Hebat?"
    tvDeskripsi.text = "Pilih satu doa dan hapalkan pelan-pelan ya. Kamu pasti bisa!"
   }
   DoaViewModel.FilterMode.NOT_MEMORIZED -> {
    ivIcon.setImageResource(R.drawable.ic_sudah_hapal)
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

 private fun setupFilterButtons() {
  // Daftar semua tombol filter
  val buttons = listOf(binding.btnFilterSemua, binding.btnFilterSudah, binding.btnFilterBelum)

  // Set Default: Tombol "Semua" aktif
  updateButtonVisual(binding.btnFilterSemua, buttons)

  // Listener Tombol SEMUA
  binding.btnFilterSemua.setOnClickListener {
   animateButton(it) // Animasi Pop
   updateButtonVisual(it, buttons) // Update Warna
   currentFilterMode = DoaViewModel.FilterMode.ALL
   viewModel.setFilter(DoaViewModel.FilterMode.ALL)
  }

  // Listener Tombol SUDAH HAPAL
  binding.btnFilterSudah.setOnClickListener {
   animateButton(it)
   updateButtonVisual(it, buttons)
   currentFilterMode = DoaViewModel.FilterMode.MEMORIZED
   viewModel.setFilter(DoaViewModel.FilterMode.MEMORIZED)
  }

  // Listener Tombol BELUM HAPAL
  binding.btnFilterBelum.setOnClickListener {
   animateButton(it)
   updateButtonVisual(it, buttons)
   currentFilterMode = DoaViewModel.FilterMode.NOT_MEMORIZED
   viewModel.setFilter(DoaViewModel.FilterMode.NOT_MEMORIZED)
  }
 }

 // Fungsi Mengatur Visual Tombol (Ganti Warna & Text Color)
 private fun updateButtonVisual(selectedBtn: View, allButtons: List<View>) {
  allButtons.forEach { btn ->
   val button = btn as androidx.appcompat.widget.AppCompatButton
   if (btn == selectedBtn) {
    // KONDISI AKTIF
    btn.isSelected = true // Ini memicu selector XML tadi jadi Orange
    btn.setTextColor(android.graphics.Color.WHITE)
    btn.elevation = 8f
   } else {
    // KONDISI TIDAK AKTIF
    btn.isSelected = false // Ini memicu selector XML tadi jadi Abu
    btn.setTextColor(android.graphics.Color.parseColor("#757575"))
    btn.elevation = 2f
   }
  }
 }

 // Fungsi Animasi "Pop" (Membal)
 private fun animateButton(view: View) {
  view.animate()
   .scaleX(1.1f)
   .scaleY(1.1f)
   .setDuration(100)
   .withEndAction {
    view.animate()
     .scaleX(1.0f)
     .scaleY(1.0f)
     .setDuration(300)
     .setInterpolator(android.view.animation.BounceInterpolator()) // Efek membal
     .start()
   }
   .start()
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
