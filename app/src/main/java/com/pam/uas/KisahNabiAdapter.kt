package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.data.local.entity.KisahNabiEntity
import com.pam.uas.databinding.ItemKisahNabiBinding

class KisahNabiAdapter(private var list: List<KisahNabiEntity>) :
    RecyclerView.Adapter<KisahNabiAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemKisahNabiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKisahNabiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvNamaNabi.text = item.name
        holder.binding.tvUsia.text = "Usia: ${item.usia} tahun"

        // Klik item untuk lihat detail cerita (bisa dibuat Intent ke DetailActivity nanti)
        holder.itemView.setOnClickListener {
            // Implementasi buka detail cerita di sini
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<KisahNabiEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}
