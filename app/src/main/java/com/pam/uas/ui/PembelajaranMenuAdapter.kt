package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.R // Pastikan import R paket kamu
import com.pam.uas.databinding.ItemPembelajaranMenuBinding

// Data class sederhana untuk menu
data class MenuModel(
    val title: String,
    val categoryKey: String, // Kunci kategori yang sama persis dengan di JSON/DB
    val imageRes: Int
)

class PembelajaranMenuAdapter(
    private val onItemClick: (MenuModel) -> Unit
) : RecyclerView.Adapter<PembelajaranMenuAdapter.VH>() {

    // Daftar Menu Statis (Sesuaikan imageRes dengan icon yang kamu punya)
    private val menuList = listOf(
        MenuModel("Rukun Islam", "RUKUN_ISLAM", R.drawable.ic_launcher_foreground),
        MenuModel("Rukun Iman", "RUKUN_IMAN", R.drawable.ic_rukun_iman),
        MenuModel("Asmaul Husna", "ASMAUL_HUSNA", R.drawable.ic_launcher_foreground),
        MenuModel("Shalat Wajib", "SHALAT_WAJIB", R.drawable.ic_launcher_foreground),
        MenuModel("Sifat Wajib Allah", "SIFAT_WAJIB_ALLAH", R.drawable.ic_launcher_foreground),
        MenuModel("Tata Cara Wudhu", "TATA_CARA_WUDHU", R.drawable.ic_launcher_foreground)
    )

    inner class VH(val binding: ItemPembelajaranMenuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPembelajaranMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = menuList[position]

        holder.binding.tvMenuTitle.text = item.title
        holder.binding.ivMenuImage.setImageResource(item.imageRes)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = menuList.size
}