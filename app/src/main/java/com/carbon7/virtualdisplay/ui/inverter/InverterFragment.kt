package com.carbon7.virtualdisplay.ui.inverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentInverterBinding
import com.carbon7.virtualdisplay.model.Measurement
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment

class InverterFragment : UpsDataVisualizerFragment() {
    override val viewModel: InverterViewModel by viewModels()

    private var _binding: FragmentInverterBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInverterBinding.inflate(inflater, container, false)

        setupView()

        return binding.root
    }

    private fun searchMeasurementValue(list: List<Measurement>?, code: String) : Float? {
        if (list != null) {
            for (measurement in list) {
                if (measurement.code == code) return measurement.value
            }
        }
        return null
    }

    private fun setupView(){
        val measurements = viewModel.filteredMeasurements.value
        binding.l11.text = searchMeasurementValue(measurements, "M039").toString()
        binding.l12.text = searchMeasurementValue(measurements, "M040").toString()
        binding.l21.text = searchMeasurementValue(measurements, "M044").toString()
        binding.l22.text = searchMeasurementValue(measurements, "M045").toString()
        binding.l31.text = searchMeasurementValue(measurements, "M075").toString()
        binding.l32.text = searchMeasurementValue(measurements, "M070").toString()
        binding.frequency.text = searchMeasurementValue(measurements, "M042").toString()
        val temperature = searchMeasurementValue(measurements, "M043")?.toString()
        binding.temperature.text = getString(R.string.t_amb) + "\n" + temperature + " " + getString(R.string.celsius)
        if (temperature != null) {
            binding.temperatureScrollBar.progress = temperature.toInt()
        }
    }
}