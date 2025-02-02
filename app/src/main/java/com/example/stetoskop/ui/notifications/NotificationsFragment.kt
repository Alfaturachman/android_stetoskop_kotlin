package com.example.stetoskop.ui.notifications

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stetoskop.databinding.FragmentNotificationsBinding
import kotlin.random.Random

class NotificationsFragment : Fragment() {
    private lateinit var lineChart: LineChart

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val lineChart: LineChart = binding.heartRateChart
        // Contoh data amplitudo dan waktu
        val amplitudes = listOf(0.1f, 0.3f, 0.5f, 0.7f, 0.9f, 0.7f, 0.5f, 0.3f, 0.1f)
        val timeInterval = 1f // Interval waktu per detik

        // Buat entri data untuk LineChart
        val entries = mutableListOf<Entry>()
        for (i in amplitudes.indices) {
            entries.add(Entry(i * timeInterval, amplitudes[i]))
        }

        // Buat dataset dan atur propertinya
        val dataSet = LineDataSet(entries, "Amplitudo").apply {
            color = android.graphics.Color.BLUE // Warna garis
            valueTextColor = android.graphics.Color.RED // Warna teks nilai
            lineWidth = 2f // Ketebalan garis
            setDrawCircles(false) // Nonaktifkan lingkaran pada titik data
            setDrawValues(false) // Nonaktifkan teks nilai
        }

        // Set data ke LineChart
        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Konfigurasi chart
        lineChart.description.text = "Waveform Suara"
        lineChart.xAxis.labelCount = amplitudes.size // Jumlah label sumbu X
        lineChart.invalidate() // Refresh chart
        val handler = Handler(Looper.getMainLooper())
        val updateChartTask = object : Runnable {
            override fun run() {
                // Tambahkan data baru
                val newAmplitude = Random.nextFloat() // Contoh data acak
                entries.add(Entry(entries.size * timeInterval, newAmplitude))

                // Perbarui chart
                lineData.notifyDataChanged()
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()

                // Jadwalkan update berikutnya
                handler.postDelayed(this, 1000) // Update setiap 1 detik
            }
        }
        handler.post(updateChartTask)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}