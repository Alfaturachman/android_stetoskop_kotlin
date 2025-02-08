package com.example.stetoskop.ui.pre_proses

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stetoskop.databinding.ActivityPreProsesBinding
import com.example.stetoskop.ui.pre_diagnosa.PreDiagnosaActivity

class PreProsesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreProsesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menggunakan View Binding
        binding = ActivityPreProsesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Button Selanjutnya
        binding.btnSelanjutnya.setOnClickListener {
            // Pindah ke activity berikutnya (Pastikan tidak loop ke dirinya sendiri)
            val intent = Intent(this@PreProsesActivity, PreDiagnosaActivity::class.java)
            startActivity(intent)
        }
    }
}
