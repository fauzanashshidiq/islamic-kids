package com.pam.uas.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pam.uas.DetailPembelajaranActivity
import com.pam.uas.databinding.FragmentPembelajaranBinding
import com.pam.uas.ui.PembelajaranMenuAdapter
import com.pam.uas.viewmodel.PembelajaranViewModel
import kotlin.getValue

class PembelajaranFragment : Fragment() {

 private var _binding: FragmentPembelajaranBinding? = null
 private val binding get() = _binding!!

 private val viewModel: PembelajaranViewModel by viewModels()

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View {
  _binding = FragmentPembelajaranBinding.inflate(inflater, container, false)
  return binding.root
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)

  viewModel.preloadPembelajaran()

  // Setup Adapter
  val adapter = PembelajaranMenuAdapter { menu ->
   // Saat item diklik, buka DetailActivity dengan membawa KEY KATEGORI
   val intent = Intent(requireContext(), DetailPembelajaranActivity::class.java)
   intent.putExtra("EXTRA_KATEGORI", menu.categoryKey) // Kirim "RUKUN_ISLAM", "SIFAT_ALLAH", dll
   intent.putExtra("EXTRA_JUDUL", menu.title) // Kirim Judul untuk Header
   startActivity(intent)
  }

  // Setup RecyclerView Grid 2 Kolom
  binding.rvPembelajaranMenu.apply {
   layoutManager = GridLayoutManager(requireContext(), 2)
   this.adapter = adapter
  }
 }

 override fun onDestroyView() {
  super.onDestroyView()
  _binding = null
 }
}
