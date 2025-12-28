package com.pam.uas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pam.uas.data.prefs.PinManager

class PinActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        val edtPin = findViewById<EditText>(R.id.edtPin)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitPin)
        val tvTitle = findViewById<TextView>(R.id.tvTitlePin)

        val isCreateMode = !PinManager.isPinExists(this)

        // Kalau belum ada PIN, berarti ini halaman buat PIN
        if (isCreateMode) {
            tvTitle.text = "Buat PIN Orang Tua"
            btnSubmit.text = "Buat PIN"
        } else {
            tvTitle.text = "Masukkan PIN"
            btnSubmit.text = "Verifikasi"
        }

        btnSubmit.setOnClickListener {
            val pin = edtPin.text.toString()

            if (pin.length < 4) {
                Toast.makeText(this, "PIN minimal 4 angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isCreateMode) {
                PinManager.savePin(this, pin)
                Toast.makeText(this, "PIN berhasil dibuat!", Toast.LENGTH_SHORT).show()
                goToAddDoa()
            } else {
                if (PinManager.checkPin(this, pin)) {
                    goToAddDoa()
                } else {
                    Toast.makeText(this, "PIN salah!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToAddDoa() {
        setResult(RESULT_OK)
        finish()
    }
}
