package com.example.stetoskop.ui.pre_diagnosa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stetoskop.R
import com.example.stetoskop.databinding.ActivityPreDiagnosaBinding
import com.example.stetoskop.ui.pre_deteksi.PreDeteksiActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class PreDiagnosaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreDiagnosaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menggunakan View Binding
        binding = ActivityPreDiagnosaBinding.inflate(layoutInflater)
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

        // Inisialisasi dan konfigurasi LineChart
        setupLineCharts()

        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Button Selanjutnya
        binding.btnSelanjutnya.setOnClickListener {
            val intent = Intent(this@PreDiagnosaActivity, PreDeteksiActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLineCharts() {
        // Data statis untuk setiap jenis sinyal
        val crepitationsData = listOf(
            Entry(0f, 0.5f),
            Entry(1f, 0.8f),
            Entry(2f, 0.6f),
            Entry(3f, 0.9f),
            Entry(4f, 0.7f)
        )

        val wheezesData = listOf(
            Entry(0f, 0.3f),
            Entry(1f, 0.5f),
            Entry(2f, 0.4f),
            Entry(3f, 0.6f),
            Entry(4f, 0.5f)
        )

        val cracklesData = listOf(
            Entry(0f, 0.7f),
            Entry(1f, 0.9f),
            Entry(2f, 0.8f),
            Entry(3f, 1.0f),
            Entry(4f, 0.9f)
        )

        val bronchialData = listOf(
            Entry(0f, 0.4f),
            Entry(1f, 0.6f),
            Entry(2f, 0.5f),
            Entry(3f, 0.7f),
            Entry(4f, 0.6f)
        )

        // Konfigurasi dan tampilkan data pada setiap LineChart
        setupChart(binding.preDiagnosaCrepitations, crepitationsData, "Crepitations", Color.BLUE)
        setupChart(binding.preDiagnosaWheezes, wheezesData, "Wheezes", Color.RED)
        setupChart(binding.preDiagnosaCrackles, cracklesData, "Crackles", Color.GREEN)
        setupChart(binding.preDiagnosaBronchial, bronchialData, "Bronchial", Color.MAGENTA)
    }

    private fun setupChart(lineChart: com.github.mikephil.charting.charts.LineChart, data: List<Entry>, label: String, color: Int) {
        val dataSet = LineDataSet(data, label).apply {
            this.color = color
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setDrawValues(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        val lineData = LineData(dataSet)
        lineChart.apply {
            setBackgroundColor(Color.WHITE)
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            axisRight.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.BLACK
                setDrawGridLines(true)
                granularity = 1f
                axisMinimum = 0f
            }

            axisLeft.apply {
                textColor = Color.BLACK
                setDrawGridLines(true)
                axisMinimum = 0f
                axisMaximum = 1.2f
            }

            this.data = lineData
            notifyDataSetChanged()
            invalidate()
        }
    }
}