package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.data.remote.response.ApiDoaResponse
import com.pam.uas.databinding.ItemApiDoaCheckboxBinding

class DoaApiCheckboxAdapter(
    private var list: List<ApiDoaResponse> = emptyList(),
    private val onItemCheckedChange: (ApiDoaResponse, Boolean) -> Unit
) : RecyclerView.Adapter<DoaApiCheckboxAdapter.VH>() {

    inner class VH(val binding: ItemApiDoaCheckboxBinding) : RecyclerView.ViewHolder(binding.root)
    private var savedTitles: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemApiDoaCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.binding.tvTitleApiDoa.text = item.doa

        val isSaved = savedTitles.contains(item.doa)

        // Hapus listener lama biar gak trigger saat scrolling
        holder.binding.cbSelect.setOnCheckedChangeListener(null)

        // Set status checkbox: TRUE jika ada di DB, FALSE jika tidak
        holder.binding.cbSelect.isChecked = isSaved

        // Aktifkan checkbox selamanya (jangan didisable)
        holder.binding.cbSelect.isEnabled = true
        holder.binding.cbSelect.alpha = 1.0f
        holder.binding.root.alpha = 1.0f

        // Pasang listener baru
        holder.binding.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            // Panggil callback ke Activity
            onItemCheckedChange(item, isChecked)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<ApiDoaResponse>) {
        list = newList
        notifyDataSetChanged()
    }

    fun setSavedIds(titles: List<String>) {
        savedTitles = titles
        notifyDataSetChanged() // Refresh tampilan adapter
    }
}
