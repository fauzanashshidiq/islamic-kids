package com.pam.uas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.pam.uas.data.prefs.PinManager

class PinActivity : AppCompatActivity() {

    private var currentPin = ""
    // Kita set target panjang PIN 6 digit agar aman dan sesuai UI
    private val MAX_PIN_LENGTH = 4

    // View Components
    private lateinit var tvPinDisplay: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvInstruction: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        // 1. Inisialisasi View sesuai ID di XML baru
        tvPinDisplay = findViewById(R.id.tvPinDisplay)
        tvTitle = findViewById(R.id.tvPinTitle)
        tvInstruction = findViewById(R.id.tvInstruction)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnDelete = findViewById<ImageButton>(R.id.btnDelete) // Tombol hapus di pojok kanan bawah keypad

        // 2. Cek Mode (Buat Baru vs Verifikasi)
        val isCreateMode = !PinManager.isPinExists(this)
        setupTextUI(isCreateMode)

        // 3. Setup Tombol Angka (0-9)
        setupKeypad(isCreateMode)

        // 4. Logic Tombol Hapus (Delete)
        btnDelete.setOnClickListener {
            if (currentPin.isNotEmpty()) {
                currentPin = currentPin.dropLast(1) // Hapus 1 angka terakhir
                updatePinDisplay()
            }
        }

        // 5. Logic Tombol Back (Pojok kiri atas)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupTextUI(isCreateMode: Boolean) {
        if (isCreateMode) {
            tvTitle.text = "Buat PIN Baru"
            tvInstruction.text = "Silakan buat 6 digit PIN\nuntuk keamanan data"
        } else {
            tvTitle.text = "Masukkan PIN"
            tvInstruction.text = "Masukkan PIN untuk masuk\nke menu Tambah Doa"
        }
    }

    private fun setupKeypad(isCreateMode: Boolean) {
        // Daftar ID tombol angka dari XML Anda
        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in buttonIds) {
            findViewById<AppCompatButton>(id).setOnClickListener { view ->
                // Jika panjang PIN belum 6, tambahkan angka
                if (currentPin.length < MAX_PIN_LENGTH) {
                    val number = (view as AppCompatButton).text.toString()
                    currentPin += number
                    updatePinDisplay()

                    // --- AUTO SUBMIT LOGIC ---
                    // Jika sudah 6 digit, langsung cek (tanpa tombol submit)
                    if (currentPin.length == MAX_PIN_LENGTH) {
                        processPin(isCreateMode)
                    }
                }
            }
        }
    }

    private fun updatePinDisplay() {
        // Mengubah tampilan menjadi titik-titik (Masking) agar aman
        // Contoh: input "123" jadi "●●●"
        tvPinDisplay.text = "●".repeat(currentPin.length)

        // Opsional: Jika ingin menampilkan angka asli sebentar, logicnya bisa diubah di sini
    }

    private fun processPin(isCreateMode: Boolean) {
        if (isCreateMode) {
            // --- LOGIC SIMPAN PIN BARU ---
            PinManager.savePin(this, currentPin)
            Toast.makeText(this, "PIN berhasil dibuat!", Toast.LENGTH_SHORT).show()
            goToAddDoa()
        } else {
            // --- LOGIC VERIFIKASI PIN ---
            if (PinManager.checkPin(this, currentPin)) {
                // JIKA BENAR -> Masuk
                goToAddDoa()
            } else {
                // JIKA SALAH -> Reset
                Toast.makeText(this, "PIN Salah!", Toast.LENGTH_SHORT).show()
                currentPin = "" // Kosongkan input
                updatePinDisplay()
            }
        }
    }

    private fun goToAddDoa() {
        setResult(RESULT_OK)
        finish()
    }
}