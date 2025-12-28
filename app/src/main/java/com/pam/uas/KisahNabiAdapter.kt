package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pam.uas.data.local.entity.KisahNabiEntity
import com.pam.uas.databinding.ItemKisahNabiBinding

class KisahNabiAdapter(
    private var list: List<KisahNabiEntity>,
    // Tambahkan callback klik
    private val onItemClick: (KisahNabiEntity) -> Unit
) : RecyclerView.Adapter<KisahNabiAdapter.VH>() {

    inner class VH(val binding: ItemKisahNabiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemKisahNabiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]

        holder.binding.tvNamaNabi.text = item.name
        // ... set text lainnya sesuai item layout kamu ...

        // Load gambar icon jika ada (opsional, pakai Glide)
        // Glide.with(holder.itemView).load(item.imageUrl).into(holder.binding.ivIconNabi)

        // KLIK ITEM
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<KisahNabiEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}
