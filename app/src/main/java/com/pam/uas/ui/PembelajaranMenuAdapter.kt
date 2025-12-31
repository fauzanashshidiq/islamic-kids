package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.R // Pastikan import R paket kamu
import com.pam.uas.databinding.ItemPembelajaranMenuBinding

// Data class sederhana untuk menu
data class MenuModel(
    val title: String,
    val categoryKey: String, // Kunci kategori yang sama persis dengan di JSON/DB
    val imageRes: Int,
    val backgroundDrawableRes: Int, // Drawable Background (Gradient + Shadow)
    val buttonTextColorRes: Int // Warna Teks Tombol "Baca Sekarang"
)

class PembelajaranMenuAdapter(
    private val onItemClick: (MenuModel) -> Unit
) : RecyclerView.Adapter<PembelajaranMenuAdapter.VH>() {

    // Daftar Menu Statis (Sesuaikan imageRes dengan icon yang kamu punya)
    private val menuList = listOf(
        MenuModel("Rukun Islam", "RUKUN_ISLAM", R.drawable.ic_rukun_islam, R.drawable.bg_3d_card_blue, R.color.card_blue_shadow),
        MenuModel("Rukun Iman", "RUKUN_IMAN", R.drawable.ic_rukun_iman, R.drawable.bg_3d_card_orange, R.color.card_orange_shadow),
        MenuModel("Sholat Wajib", "SHALAT_WAJIB", R.drawable.ic_shalat_wajib, R.drawable.bg_3d_card_purple, R.color.card_purple_shadow),
        MenuModel("Sifat Wajib Allah", "SIFAT_WAJIB_ALLAH", R.drawable.ic_sifat_wajib, R.drawable.bg_3d_card_green, R.color.card_green_shadow),
        MenuModel("Tata Cara Wudhu", "TATA_CARA_WUDHU", R.drawable.ic_berwudhu, R.drawable.bg_3d_card_orange, R.color.card_orange_shadow),
        MenuModel("Asmaul Husna", "ASMAUL_HUSNA", R.drawable.ic_asmaul_husna, R.drawable.bg_3d_card_peach, R.color.card_peach_shadow)
    )

    inner class VH(val binding: ItemPembelajaranMenuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPembelajaranMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = menuList[position]

        // Set Image (Gambar menu)
        holder.binding.ivMenuImage.setImageResource(item.imageRes)

        // Set Background Drawable (Gradient + 3D Effect)
        holder.binding.layoutMain.setBackgroundResource(item.backgroundDrawableRes)

        // Set Warna Teks Tombol "Baca Sekarang" agar serasi dengan tema card
        val textColor = ContextCompat.getColor(holder.itemView.context, item.buttonTextColorRes)
        holder.binding.btnBacaSekarang.setTextColor(textColor)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = menuList.size
}