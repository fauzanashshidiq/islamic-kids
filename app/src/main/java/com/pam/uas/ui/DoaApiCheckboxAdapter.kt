package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.data.remote.response.ApiDoaResponse
import com.pam.uas.databinding.ItemApiDoaCheckboxBinding

class DoaApiCheckboxAdapter(
    private var list: List<ApiDoaResponse> = emptyList()
) : RecyclerView.Adapter<DoaApiCheckboxAdapter.VH>() {

    inner class VH(val binding: ItemApiDoaCheckboxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemApiDoaCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]

        holder.binding.tvTitleApiDoa.text = item.doa

        holder.binding.cbSelect.setOnCheckedChangeListener(null)
        holder.binding.cbSelect.isChecked = item.isChecked

        holder.binding.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<ApiDoaResponse>) {
        list = newList
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<ApiDoaResponse> = list.filter { it.isChecked }
}
