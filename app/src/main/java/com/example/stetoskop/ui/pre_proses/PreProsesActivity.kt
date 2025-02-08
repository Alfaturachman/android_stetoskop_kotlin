package com.example.stetoskop.ui.pre_proses

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.stetoskop.R
import com.example.stetoskop.databinding.ActivityPreProsesBinding
import com.example.stetoskop.ui.pre_diagnosa.PreDiagnosaActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class PreProsesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreProsesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreProsesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.white, theme)

        // Inisialisasi
        setupLineChart()
        showFilterChart()

        // Button Filter
        binding.btnFilter.setOnClickListener {
            showFilterChart()
            binding.tvChartInfo.text = "Filter"
        }

        // Button Amplify
        binding.btnAmplify.setOnClickListener {
            showAmplifyChart()
            binding.tvChartInfo.text = "Amplify"
        }

        // Button Endpoint
        binding.btnEndpoint.setOnClickListener {
            showEndpointChart()
            binding.tvChartInfo.text = "End-Point Detection"
        }

        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Button Selanjutnya
        binding.btnSelanjutnya.setOnClickListener {
            // Pindah ke activity berikutnya (Pastikan tidak loop ke dirinya sendiri)
            val intent = Intent(this@PreProsesActivity, PreDiagnosaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLineChart() {
        binding.preDiagnosa.apply {
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
        }
    }

    private fun showFilterChart() {
        // Data dummy untuk Filter
        val filterData = listOf(
            Entry(0f, 0.2f),
            Entry(1f, 0.5f),
            Entry(2f, 0.3f),
            Entry(3f, 0.7f),
            Entry(4f, 0.4f)
        )

        updateChart(filterData, "Filter", Color.BLUE)
    }

    private fun showAmplifyChart() {
        // Data dummy untuk Amplify
        val amplifyData = listOf(
            Entry(0f, 0.4f),
            Entry(1f, 0.6f),
            Entry(2f, 0.5f),
            Entry(3f, 0.8f),
            Entry(4f, 0.7f)
        )

        updateChart(amplifyData, "Amplify", Color.RED)
    }

    private fun showEndpointChart() {
        // Data dummy untuk End-Point
        val endpointData = listOf(
            Entry(0f, 0.1f),
            Entry(1f, 0.3f),
            Entry(2f, 0.2f),
            Entry(3f, 0.5f),
            Entry(4f, 0.4f)
        )

        updateChart(endpointData, "End-Point", Color.GREEN)
    }

    private fun updateChart(data: List<Entry>, label: String, color: Int) {
        val dataSet = LineDataSet(data, label).apply {
            this.color = color
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setDrawValues(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        val lineData = LineData(dataSet)
        binding.preDiagnosa.apply {
            this.data = lineData
            notifyDataSetChanged()
            invalidate()
        }
    }
}