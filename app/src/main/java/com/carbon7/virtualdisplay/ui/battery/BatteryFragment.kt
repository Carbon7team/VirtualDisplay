package com.carbon7.virtualdisplay.ui.battery

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentBatteryBinding
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment

class BatteryFragment : UpsDataVisualizerFragment() {
    override val viewModel: BatteryViewModel by viewModels()

    private var _binding: FragmentBatteryBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBatteryBinding.inflate(inflater, container, false)

        setupView()

        return binding.root
    }

    private fun timeConverter(time : Int) : String {
        val hours = time / 3600
        val minutes = (time % 3600) / 60
        val seconds = time % 60
        return "$hours:$minutes:$seconds"
    }

    @SuppressLint("ResourceAsColor")
    private fun colorProgressBar(progress : Int, progressBar : ProgressBar) {
        when {
            progress < 31 -> {
                progressBar.progressTintList = ColorStateList.valueOf(R.color.green)
            }
            progress < 61 -> {
                progressBar.progressTintList = ColorStateList.valueOf(R.color.orange)
            }
            else -> {
                progressBar.progressTintList = ColorStateList.valueOf(R.color.red)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(){
        viewModel.measurements.observe(viewLifecycleOwner) {
            binding.v1.text = getString(R.string.positive_battery) + "\n" + it[16].value!!.toInt().toString() + " " + getString(R.string.V)
            binding.v2.text = getString(R.string.negative_battery) + "\n" + it[17].value!!.toInt().toString() + " " + getString(R.string.V)
            binding.a1.text = getString(R.string.positive_battery) + "\n" + it[18].value!!.toInt().toString() + " " + getString(R.string.A)
            binding.a2.text = getString(R.string.negative_battery) + "\n" + it[19].value!!.toInt().toString() + " " + getString(R.string.A)
            binding.b1.text = getString(R.string.backup_time) + "\n" + timeConverter(it[24].value!!.toInt())
            binding.b2.text = getString(R.string.time_on_battery) + "\n" + timeConverter(it[25].value!!.toInt())
            binding.b3.text = getString(R.string.capacity) + "\n" + it[22].value!!.toInt().toString() + " " + getString(R.string.perc)
            binding.b4.text = getString(R.string.capacity) + "\n" + it[23].value!!.toInt().toString() + " " + getString(R.string.Ah)
            binding.t1.text = getString(R.string.t_inst) + "\n" + it[26].value!!.toString() + " " + getString(R.string.celsius)
            binding.t2.text = getString(R.string.t_avg) + "\n" + it[27].value!!.toString() + " " + getString(R.string.celsius)
            val battery = binding.battery
            battery.progress = it[22].value!!.toInt()
            colorProgressBar(battery.progress, battery)
            val t1ProgressBar = binding.t1ProgressBar
            t1ProgressBar.progress = it[26].value!!.toInt()
            colorProgressBar(t1ProgressBar.progress, t1ProgressBar)
            val t2ProgressBar = binding.t1ProgressBar
            t2ProgressBar.progress = it[27].value!!.toInt()
            colorProgressBar(t2ProgressBar.progress, t2ProgressBar)
        }
    }
}