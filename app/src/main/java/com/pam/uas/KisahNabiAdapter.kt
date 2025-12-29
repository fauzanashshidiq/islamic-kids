package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pam.uas.R
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
        val context = holder.itemView.context

        holder.binding.tvNamaNabi.text = "Kisah " + item.name

         if (!item.iconUrl.isNullOrEmpty()) {
            // Ambil ID resource berdasarkan nama string (misal: "ic_nabi_adam"
            val resourceId = context.resources.getIdentifier(
                item.iconUrl,
                "drawable",
                context.packageName
            )

            // Cek apakah resource ditemukan (tidak 0)
            if (resourceId != 0) {
                Glide.with(context)
                    .load(resourceId) // Load pakai ID (Integer)
                    .into(holder.binding.ivIconNabi)
            } else {
                // Kalau nama file salah/tidak ditemukan di drawable
                holder.binding.ivIconNabi.setImageResource(R.drawable.ic_launcher_foreground)
            }
        } else {
            holder.binding.ivIconNabi.setImageResource(R.drawable.ic_launcher_foreground)
        }

        val warnaResId = when {
            item.name.contains("Adam", ignoreCase = true) -> R.color.color_nabi_adam
            item.name.contains("Idris", ignoreCase = true) -> R.color.color_nabi_idris
            // Tambahkan nabi lainnya...
            else -> R.color.white // Warna default (Putih)
        }

        // Terapkan warna ke CardView
        // `holder.binding.root` merujuk ke elemen root di layout item (CardView)
        holder.binding.root.setCardBackgroundColor(
            androidx.core.content.ContextCompat.getColor(context, warnaResId)
        )

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
