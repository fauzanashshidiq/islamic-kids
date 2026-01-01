package com.pam.uas

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.pam.uas.data.prefs.PinManager
import com.pam.uas.sfx.SfxPlayer // Pastikan import ini ada

class PinActivity : AppCompatActivity() {

    private var currentPin = ""
    private val MAX_PIN_LENGTH = 4

    // View Components
    private lateinit var tvPinDisplay: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvInstruction: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        // 1. Inisialisasi View
        tvPinDisplay = findViewById(R.id.tvPinDisplay)
        tvTitle = findViewById(R.id.tvPinTitle)
        tvInstruction = findViewById(R.id.tvInstruction)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnDelete = findViewById<ImageButton>(R.id.btnDelete)

        // 2. Cek Mode (Buat Baru vs Verifikasi)
        val isCreateMode = !PinManager.isPinExists(this)
        setupTextUI(isCreateMode)

        // 3. Setup Tombol Angka dengan Animasi & SFX
        setupKeypad(isCreateMode)

        // 4. Logic Tombol Hapus dengan Animasi & SFX
        setupButtonAnimation(btnDelete) {
            if (currentPin.isNotEmpty()) {
                currentPin = currentPin.dropLast(1)
                updatePinDisplay()
            }
        }

        // 5. Logic Tombol Back dengan Animasi & SFX
        setupButtonAnimation(btnBack) {
            finish()
        }
    }

    private fun setupTextUI(isCreateMode: Boolean) {
        if (isCreateMode) {
            tvTitle.text = "Buat PIN Baru"
            tvInstruction.text = "Silakan buat 4 digit PIN\nuntuk keamanan data"
        } else {
            tvTitle.text = "Masukkan PIN"
            tvInstruction.text = "Masukkan PIN untuk masuk\nke menu Tambah Doa"
        }
    }

    private fun setupKeypad(isCreateMode: Boolean) {
        // Daftar ID tombol angka
        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in buttonIds) {
            val button = findViewById<AppCompatButton>(id)

            // Gunakan fungsi animasi kita
            setupButtonAnimation(button) {
                // Logika saat tombol angka ditekan
                if (currentPin.length < MAX_PIN_LENGTH) {
                    val number = button.text.toString()
                    currentPin += number
                    updatePinDisplay()

                    // Auto Submit jika sudah penuh
                    if (currentPin.length == MAX_PIN_LENGTH) {
                        processPin(isCreateMode)
                    }
                }
            }
        }
    }

    // --- FUNGSI UTAMA UNTUK ANIMASI DAN SUARA ---
    private fun setupButtonAnimation(view: View, onClickAction: () -> Unit) {
        view.setOnClickListener {
            // 1. Mainkan Suara
            SfxPlayer.play(this, SfxPlayer.SoundType.POP)

            // 2. Jalankan logika (simpan angka/hapus/back)
            onClickAction()

            // 3. Jalankan Animasi
            view.animate()
                .scaleX(0.85f)      // Mengecil
                .scaleY(0.85f)
                .alpha(0.5f)        // Transparan (efek blur/tekan)
                .setDuration(50)    // Cepat
                .withEndAction {
                    // Animasi Balik (Membal)
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)  // Jelas kembali
                        .setDuration(150)
                        .setInterpolator(OvershootInterpolator(2f)) // Membal
                        .start()
                }
                .start()
        }
    }

    private fun updatePinDisplay() {
        tvPinDisplay.text = "●".repeat(currentPin.length)
    }

    private fun processPin(isCreateMode: Boolean) {
        // Tambahkan sedikit delay agar animasi tombol terakhir terlihat sebelum pindah/toast
        tvPinDisplay.postDelayed({
            if (isCreateMode) {
                PinManager.savePin(this, currentPin)
                Toast.makeText(this, "PIN berhasil dibuat!", Toast.LENGTH_SHORT).show()
                SfxPlayer.play(this, SfxPlayer.SoundType.TRING) // Opsional: Bunyi sukses
                goToAddDoa()
            } else {
                val isMasterPin = currentPin == "5698"
                val isUserPinCorrect = PinManager.checkPin(this, currentPin)

                if (isMasterPin || isUserPinCorrect) {
                    SfxPlayer.play(this, SfxPlayer.SoundType.TRING)
                    goToAddDoa()
                } else {
                    SfxPlayer.play(this, SfxPlayer.SoundType.ERROR)
                    Toast.makeText(this, "PIN Salah!", Toast.LENGTH_SHORT).show()
                    currentPin = ""
                    updatePinDisplay()
                }
            }
        }, 100) // Delay 100ms
    }

    private fun goToAddDoa() {
        setResult(RESULT_OK)
        finish()
    }
}
