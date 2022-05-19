package com.carbon7.virtualdisplay.ui.bypass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentBypassBinding
import com.carbon7.virtualdisplay.model.Measurement
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment

class BypassFragment : UpsDataVisualizerFragment() {
    override val viewModel: BypassViewModel by viewModels()

    private var _binding: FragmentBypassBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBypassBinding.inflate(inflater, container, false)

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

        if (measurements != null) {
            Log.d("==>", measurements.size.toString())
        } else {
            Log.d("==>", "measurements è nullo")
        }

        binding.l11.text = searchMeasurementValue(measurements, "M039").toString()
        binding.l12.text = searchMeasurementValue(measurements, "M040").toString()
        binding.l13.text = searchMeasurementValue(measurements, "M041").toString()
        binding.l14.text = searchMeasurementValue(measurements, "M043").toString()
        binding.l21.text = searchMeasurementValue(measurements, "M044").toString()
        binding.l22.text = searchMeasurementValue(measurements, "M045").toString()
        binding.l23.text = searchMeasurementValue(measurements, "M073").toString()
        binding.l24.text = searchMeasurementValue(measurements, "M074").toString()
        binding.l31.text = searchMeasurementValue(measurements, "M075").toString()
        binding.l32.text = searchMeasurementValue(measurements, "M070").toString()
        binding.l33.text = searchMeasurementValue(measurements, "M071").toString()
        binding.l34.text = searchMeasurementValue(measurements, "M072").toString()
        binding.frequency.text =  searchMeasurementValue(measurements, "M042").toString() + " " + getString(R.string.herz)
    }
}