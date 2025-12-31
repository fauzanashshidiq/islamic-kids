package com.pam.uas.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pam.uas.data.remote.response.ApiDoaResponse
import com.pam.uas.databinding.ItemApiDoaCheckboxBinding
import com.pam.uas.sfx.SfxPlayer

class DoaApiCheckboxAdapter(
    private var list: List<ApiDoaResponse> = emptyList(),
    private val onCheckChanged: (ApiDoaResponse, Boolean) -> Unit,
    private val onSaveNote: (ApiDoaResponse, String) -> Unit
) : RecyclerView.Adapter<DoaApiCheckboxAdapter.VH>() {

    inner class VH(val binding: ItemApiDoaCheckboxBinding) : RecyclerView.ViewHolder(binding.root)
    private var savedNotesMap: Map<String, String> = emptyMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemApiDoaCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.binding.tvTitleApiDoa.text = item.doa

        val savedNote = savedNotesMap[item.doa]
        val isSaved = savedNote != null

        // Reset listener dulu
        holder.binding.cbSelect.setOnCheckedChangeListener(null)
        holder.binding.btnSimpanCatatan.setOnClickListener(null)

        // ATUR STATUS UI BERDASARKAN DB
        if (isSaved) {
            holder.binding.cbSelect.isChecked = true
            holder.binding.layoutCatatan.visibility = View.VISIBLE
            // Isi EditText dengan catatan dari DB (atau kosong jika stringnya "")
            holder.binding.etCatatan.setText(savedNote)
        } else {
            holder.binding.cbSelect.isChecked = false
            holder.binding.layoutCatatan.visibility = View.GONE
            holder.binding.etCatatan.text.clear()
        }

        // LISTENER CHECKBOX
        holder.binding.cbSelect.setOnCheckedChangeListener { buttonView, isChecked ->
            val context = buttonView.context
            if (isChecked) {
                SfxPlayer.play(context, SfxPlayer.SoundType.SUCCESS)
                // Tampilkan layout catatan
                holder.binding.layoutCatatan.visibility = View.VISIBLE
                // Simpan doa ke DB (catatan default kosong dulu)
                onCheckChanged(item, true)
            } else {
                SfxPlayer.play(context, SfxPlayer.SoundType.POP)
                // Sembunyikan layout catatan
                holder.binding.layoutCatatan.visibility = View.GONE
                holder.binding.etCatatan.text.clear()
                // Hapus doa dari DB
                onCheckChanged(item, false)
            }
        }

        // LISTENER TOMBOL SIMPAN CATATAN
        holder.binding.btnSimpanCatatan.setOnClickListener { view ->
            SfxPlayer.play(view.context, SfxPlayer.SoundType.POP)
            // 1. Animasi Mengecil (Tekan)
            view.animate()
                .scaleX(0.85f) // Mengecil ke 85%
                .scaleY(0.85f)
                .setDuration(100)
                .withEndAction {
                    // 2. Animasi Membal (Bounce Back)
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .setInterpolator(android.view.animation.BounceInterpolator()) // Efek kenyal
                        .start()

                    // 3. Jalankan logika simpan setelah animasi tekan selesai
                    val catatanBaru = holder.binding.etCatatan.text.toString()
                    onSaveNote(item, catatanBaru)
                    Toast.makeText(holder.itemView.context, "Catatan disimpan! ✅", Toast.LENGTH_SHORT).show()
                }
                .start()
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<ApiDoaResponse>) {
        list = newList
        notifyDataSetChanged()
    }

    fun setSavedData(notesMap: Map<String, String>) {
        savedNotesMap = notesMap
        notifyDataSetChanged()
    }
}
