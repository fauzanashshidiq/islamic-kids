package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.data.local.entity.DoaEntity
import com.pam.uas.databinding.ItemDoaMainBinding

class DoaMainAdapter(private var list: List<DoaEntity>) : RecyclerView.Adapter<DoaMainAdapter.VH>() {

    inner class VH(val binding: ItemDoaMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDoaMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.binding.tvJudulMain.text = item.doa
        holder.binding.tvArtiMain.text = item.artinya
        holder.binding.tvAyat.text = item.ayat
        holder.binding.tvLatin.text = item.latin ?: ""
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<DoaEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}
