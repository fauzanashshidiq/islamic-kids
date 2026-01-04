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
import com.pam.uas.DetailRukunIslamActivity
import com.pam.uas.DetailRukunImanActivity
import com.pam.uas.DetailAsmaulHusnaActivity
import com.pam.uas.DetailShalatWajibActivity
import com.pam.uas.DetailSifatWajibAllahActivity
import com.pam.uas.DetailTataCaraWudhuActivity
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
   val destinationActivity = when (menu.categoryKey) {
    "RUKUN_ISLAM" -> DetailRukunIslamActivity::class.java
    "RUKUN_IMAN" -> DetailRukunImanActivity::class.java
    "ASMAUL_HUSNA" -> DetailAsmaulHusnaActivity::class.java
    "SHALAT_WAJIB" -> DetailShalatWajibActivity::class.java
    "SIFAT_WAJIB_ALLAH" -> DetailSifatWajibAllahActivity::class.java
    "TATA_CARA_WUDHU" -> DetailTataCaraWudhuActivity::class.java
    else -> DetailPembelajaranActivity::class.java
   }

   val intent = Intent(requireContext(), destinationActivity)

   intent.putExtra("EXTRA_KATEGORI", menu.categoryKey)
   intent.putExtra("EXTRA_JUDUL", menu.title)
   startActivity(intent)
  }

  // Setup RecyclerView
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
