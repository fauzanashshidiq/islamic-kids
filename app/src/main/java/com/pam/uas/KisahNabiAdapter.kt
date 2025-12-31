package com.pam.uas.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pam.uas.R
import com.pam.uas.data.local.entity.KisahNabiEntity
import com.pam.uas.databinding.ItemKisahNabiBinding
import com.pam.uas.sfx.SfxPlayer

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
            item.name.contains("Adam", ignoreCase = true) -> R.color.color_kisah_blue
            item.name.contains("Idris", ignoreCase = true) -> R.color.color_kisah_green
            item.name.contains("Nuh", ignoreCase = true) -> R.color.color_kisah_yellow
            item.name.contains("Hud", ignoreCase = true) -> R.color.color_kisah_purple
            item.name.contains("Sholeh", ignoreCase = true) -> R.color.color_kisah_red
            item.name.contains("Ibrahim", ignoreCase = true) -> R.color.color_kisah_blue
            item.name.contains("Luth", ignoreCase = true) -> R.color.color_kisah_green
            item.name.contains("Ismail", ignoreCase = true) -> R.color.color_kisah_yellow
            item.name.contains("Ishaq", ignoreCase = true) -> R.color.color_kisah_purple
            item.name.contains("Yaqub", ignoreCase = true) -> R.color.color_kisah_red
            item.name.contains("Yusuf", ignoreCase = true) -> R.color.color_kisah_blue
            item.name.contains("Ayyub", ignoreCase = true) -> R.color.color_kisah_green
            item.name.contains("Syu'aib", ignoreCase = true) -> R.color.color_kisah_yellow
            item.name.contains("Musa", ignoreCase = true) -> R.color.color_kisah_purple
            item.name.contains("Harun", ignoreCase = true) -> R.color.color_kisah_red
            item.name.contains("Dzulkifli", ignoreCase = true) -> R.color.color_kisah_blue
            item.name.contains("Daud", ignoreCase = true) -> R.color.color_kisah_green
            item.name.contains("Sulaiman", ignoreCase = true) -> R.color.color_kisah_yellow
            item.name.contains("Ilyasa'", ignoreCase = true) -> R.color.color_kisah_red
            item.name.contains("Ilyas", ignoreCase = true) -> R.color.color_kisah_purple
            item.name.contains("Yunus", ignoreCase = true) -> R.color.color_kisah_blue
            item.name.contains("Zakariya", ignoreCase = true) -> R.color.color_kisah_green
            item.name.contains("Yahya", ignoreCase = true) -> R.color.color_kisah_yellow
            item.name.contains("Isa", ignoreCase = true) -> R.color.color_kisah_purple
            item.name.contains("Muhammad", ignoreCase = true) -> R.color.color_kisah_red
            else -> R.color.white // Warna default (Putih)
        }

        // A. Ambil Warna Utama (Integer)
        val colorMain = androidx.core.content.ContextCompat.getColor(context, warnaResId)

        // B. Buat Warna Shadow (Otomatis digelapkan 20% dari warna utama)
        // blendARGB mencampur warna utama dengan Hitam (0.2f artinya 20% hitam)
        val colorShadow = ColorUtils.blendARGB(colorMain, Color.BLACK, 0.2f)

        // C. Terapkan ke Layer View di XML
        // Gunakan background.setTint() karena background-nya adalah Drawable Shape
        holder.binding.layoutMain.background.setTint(colorMain)   // Layer Atas
        holder.binding.viewShadow.background.setTint(colorShadow) // Layer Bawah (Bayangan)


        // KLIK ITEM
        holder.itemView.setOnClickListener { view ->
            SfxPlayer.play(view.context, SfxPlayer.SoundType.POP)
            // 1. Animasi Mengecil (Efek Ditekan)
            view.animate()
                .scaleX(0.95f) // Kecilkan dikit ke 95%
                .scaleY(0.95f)
                .setDuration(100) // Durasi cepat (0.1 detik)
                .withEndAction {
                    // 2. Animasi Membal Balik (Bounce)
                    view.animate()
                        .scaleX(1.0f) // Balik ke ukuran normal
                        .scaleY(1.0f)
                        .setDuration(300)
                        .setInterpolator(android.view.animation.BounceInterpolator()) // Efek kenyal
                        .start()

                    // 3. Panggil Callback Navigasi (Pindah Layar)
                    // Ditaruh di sini supaya animasi 'tekan' selesai dulu baru pindah
                    onItemClick(item)
                }
                .start()
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<KisahNabiEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}
