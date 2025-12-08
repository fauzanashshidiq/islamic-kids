package com.pam.uas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.R
import com.pam.uas.data.local.entity.DoaEntity

class DoaAdapter(private var list: List<DoaEntity>) :
    RecyclerView.Adapter<DoaAdapter.DoaViewHolder>() {

    inner class DoaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.tvJudul)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doa, parent, false)
        return DoaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoaViewHolder, position: Int) {
        holder.tvJudul.text = list[position].judul
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<DoaEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}
