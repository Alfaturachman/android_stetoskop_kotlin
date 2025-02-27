package com.example.stetoskop.ui.sinyal

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.stetoskop.databinding.FragmentSinyalBinding
import com.example.stetoskop.ui.pre_proses.PreProsesActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.sin
import kotlin.math.PI

class SinyalFragment : Fragment() {
    private var _binding: FragmentSinyalBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineChart: LineChart
    private val entries = mutableListOf<Entry>()
    private val handler = Handler(Looper.getMainLooper())
    private var time = 0f // Waktu dalam detik

    // Runnable untuk update data real-time
    private val updateChartRunnable = object : Runnable {
        override fun run() {
            if (time >= 60) return // Stop setelah 60 detik

            // Simulasi pola napas normal menggunakan fungsi sinus
            val frequency = 0.2 // Frekuensi per detik
            val amplitude = 1.0 // Amplitudo maksimal
            val newY = (amplitude * sin(2 * PI * frequency * time)).toFloat()
            entries.add(Entry(time, newY))
            time += 0.1f // Update setiap 100 ms (0.1 detik)

            // Update grafik
            val dataSet = LineDataSet(entries, "Amplitude").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setDrawValues(false)
                setDrawCircles(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER // Membuat kurva halus
            }

            val lineData = LineData(dataSet)
            lineChart.data = lineData
            lineChart.notifyDataSetChanged()
            lineChart.invalidate() // Refresh grafik

            // Jalankan lagi setelah 100ms
            handler.postDelayed(this, 100)

            // Button Selanjutnya
            binding.btnSelanjutnya.setOnClickListener {
                // Buat Intent untuk berpindah ke PreProsesActivity
                val intent = Intent(activity, PreProsesActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSinyalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lineChart = binding.heartRateChart
        setupChart()

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Mulai update grafik real-time
        handler.post(updateChartRunnable)

        return root
    }

    private fun setupChart() {
        lineChart.apply {
            setBackgroundColor(Color.WHITE)
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            axisRight.isEnabled = false // Hilangkan sumbu Y di kanan
        }

        // Konfigurasi sumbu X (waktu dalam detik)
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
            setDrawGridLines(true)
            granularity = 1f
            axisMinimum = 0f
            labelRotationAngle = 0f
        }

        // Konfigurasi sumbu Y (amplitudo)
        lineChart.axisLeft.apply {
            textColor = Color.BLACK
            setDrawGridLines(true)
            axisMinimum = -1.2f // Batas bawah amplitudo
            axisMaximum = 1.2f // Batas atas amplitudo
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacks(updateChartRunnable) // Hentikan update jika fragment dihancurkan
    }
}