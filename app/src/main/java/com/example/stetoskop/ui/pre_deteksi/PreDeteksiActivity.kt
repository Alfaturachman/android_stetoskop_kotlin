package com.example.stetoskop.ui.pre_deteksi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stetoskop.MainActivity
import com.example.stetoskop.R
import com.example.stetoskop.databinding.ActivityPreDeteksiBinding
import com.example.stetoskop.databinding.ActivityPreDiagnosaBinding
import com.example.stetoskop.databinding.ActivityPreProsesBinding
import com.example.stetoskop.ui.pre_diagnosa.PreDiagnosaActivity

class PreDeteksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreDeteksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menggunakan View Binding
        binding = ActivityPreDeteksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.white, theme)

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Button Selanjutnya
        binding.btnSelanjutnya.setOnClickListener {
            // Pindah ke activity berikutnya (Pastikan tidak loop ke dirinya sendiri)
            val intent = Intent(this@PreDeteksiActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}