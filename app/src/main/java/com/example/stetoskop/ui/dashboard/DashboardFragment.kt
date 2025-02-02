package com.example.stetoskop.ui.dashboard

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stetoskop.R
import com.example.stetoskop.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var heartRateChart: LineChart
    private var time = 0f // Waktu awal untuk data real-time
    private lateinit var dataSet: LineDataSet
    private lateinit var lineData: LineData
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnPlaySound: Button = binding.btnPlaySound
        val progressBar: ProgressBar = binding.progressBar
        val textViewCurrentTime: TextView = binding.textViewCurrentTime
        val textViewTotalDuration: TextView = binding.textViewTotalDuration

        // Setup media player
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sound)

        // Tampilkan durasi total saat pertama kali
        val totalDuration = mediaPlayer?.duration ?: 0
        textViewTotalDuration.text = formatTime(totalDuration)

        btnPlaySound.setOnClickListener {
            mediaPlayer?.start()
            progressBar.visibility = View.VISIBLE

            // Update progress bar dan waktu secara real-time
            Thread {
                while (mediaPlayer?.isPlaying == true) {
                    val currentPosition = mediaPlayer?.currentPosition ?: 0
                    val totalDuration = mediaPlayer?.duration ?: 1
                    val progress = ((currentPosition / totalDuration.toFloat()) * 100).toInt()

                    // Update UI pada main thread
                    requireActivity().runOnUiThread {
                        progressBar.progress = progress
                        textViewCurrentTime.text = formatTime(currentPosition)
                    }

                    try {
                        Thread.sleep(100)  // Update setiap 100ms
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }

        // Inisialisasi LineChart
        heartRateChart = binding.heartRateChart
        setupChart()

        // Memulai update data real-time
        startRealTimeUpdate()

        return root
    }

    private fun setupChart() {
        val entries = ArrayList<Entry>()

        // **Tambahkan minimal 1 data awal untuk mencegah error**
        entries.add(Entry(0f, 0f))

        dataSet = LineDataSet(entries, "Detak Jantung")
        dataSet.color = Color.RED
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)

        lineData = LineData(dataSet)
        heartRateChart.data = lineData

        val desc = Description()
        desc.text = "Sinyal Detak Jantung"
        heartRateChart.description = desc

        heartRateChart.invalidate() // Refresh chart
    }

    private fun startRealTimeUpdate() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (time >= 60f) return // Hentikan setelah 60 detik

                // Simulasi sinyal detak jantung (dengan noise kecil)
                val amplitude = Math.sin(2 * Math.PI * 1.5 * time) + Random.nextFloat() * 0.2f
                val entry = Entry(time, amplitude.toFloat())

                // Pastikan `dataSet` tidak kosong sebelum menambahkan data baru
                if (dataSet.entryCount > 0) {
                    dataSet.addEntry(entry)
                    lineData.notifyDataChanged()
                    heartRateChart.notifyDataSetChanged()
                    heartRateChart.invalidate()
                }

                time += 0.1f // Tambah waktu per update (0.1 detik)
                handler.postDelayed(this, 100) // Update setiap 100ms
            }
        }, 100)
    }

    // Fungsi untuk mengonversi milidetik ke format 00:00
    private fun formatTime(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer?.release()  // Release MediaPlayer when Fragment is destroyed
        handler.removeCallbacksAndMessages(null) // Hentikan update saat Fragment dihancurkan
    }
}