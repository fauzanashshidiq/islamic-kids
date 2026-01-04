package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.R // Pastikan import R paket kamu
import com.pam.uas.databinding.ItemPembelajaranMenuBinding
import com.pam.uas.sfx.SfxPlayer

data class MenuModel(
    val title: String,
    val categoryKey: String,
    val imageRes: Int,
    val backgroundDrawableRes: Int,
    val buttonTextColorRes: Int
)

class PembelajaranMenuAdapter(
    private val onItemClick: (MenuModel) -> Unit
) : RecyclerView.Adapter<PembelajaranMenuAdapter.VH>() {
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

        holder.binding.ivMenuImage.setImageResource(item.imageRes)

        holder.binding.layoutMain.setBackgroundResource(item.backgroundDrawableRes)

        val textColor = ContextCompat.getColor(holder.itemView.context, item.buttonTextColorRes)
        holder.binding.btnBacaSekarang.setTextColor(textColor)

        holder.itemView.setOnClickListener { view ->
            SfxPlayer.play(view.context, SfxPlayer.SoundType.POP)
            view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .setInterpolator(android.view.animation.BounceInterpolator())
                        .start()
                    onItemClick(item)
                }
                .start()
        }
    }

    override fun getItemCount(): Int = menuList.size
}
