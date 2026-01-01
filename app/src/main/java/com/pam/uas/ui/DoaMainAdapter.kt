package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.data.local.entity.DoaEntity
import com.pam.uas.databinding.ItemDoaMainBinding

class DoaMainAdapter(
    private var list: List<DoaEntity>,
    private val onMemorizedChanged: (DoaEntity, Boolean) -> Unit
) : RecyclerView.Adapter<DoaMainAdapter.VH>() {

    inner class VH(val binding: ItemDoaMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDoaMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]


        // Set Data Text
        holder.binding.tvJudulMain.text = item.doa
        holder.binding.tvArtiMain.text = item.artinya
        holder.binding.tvAyat.text = item.ayat
        holder.binding.tvLatin.text = item.latin ?: ""

        // 1. Hapus listener lama dulu
        holder.binding.cbHapal.setOnCheckedChangeListener(null)

        // 2. Set Status Visual Checkbox (Visual hijau/putih otomatis ikut ini)
        holder.binding.cbHapal.isChecked = item.isMemorized

        // 3. Listener perubahan status hapal
        holder.binding.cbHapal.setOnCheckedChangeListener { buttonView, isChecked ->
            // Update data model di memory agar tidak lompat saat scroll
            item.isMemorized = isChecked

            // Panggil callback ke Fragment/ViewModel untuk update ke Database
            onMemorizedChanged(item, isChecked)
        }

        // Tampilkan catatan jika ada
        if (item.catatan.isNullOrEmpty()) {
            holder.binding.layoutCatatanItem.visibility = View.GONE
        } else {
            holder.binding.layoutCatatanItem.visibility = View.VISIBLE
            holder.binding.tvCatatan.text = "Catatan: ${item.catatan}"
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<DoaEntity>) {
        list = newList.sortedBy { it.doa }
        notifyDataSetChanged()
    }
}
