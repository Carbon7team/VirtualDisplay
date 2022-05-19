package com.carbon7.virtualdisplay.ui.rectifier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentRectifierBinding
import com.carbon7.virtualdisplay.model.Measurement
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment

class RectifierFragment : UpsDataVisualizerFragment() {
    override val viewModel: RectifierViewModel by viewModels()

    private var _binding: FragmentRectifierBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRectifierBinding.inflate(inflater, container, false)

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
        binding.l11.text = searchMeasurementValue(measurements, "M032").toString()
        binding.l12.text = searchMeasurementValue(measurements, "M033").toString()
        binding.l13.text = searchMeasurementValue(measurements, "M034").toString()
        binding.l14.text = searchMeasurementValue(measurements, "M036").toString()
        binding.l21.text = searchMeasurementValue(measurements, "M037").toString()
        binding.l22.text = searchMeasurementValue(measurements, "M038").toString()
        binding.l23.text = searchMeasurementValue(measurements, "M067").toString()
        binding.l24.text = searchMeasurementValue(measurements, "M068").toString()
        binding.l31.text = searchMeasurementValue(measurements, "M069").toString()
        binding.l32.text = searchMeasurementValue(measurements, "M064").toString()
        binding.l33.text = searchMeasurementValue(measurements, "M065").toString()
        binding.l34.text = searchMeasurementValue(measurements, "M066").toString()
        binding.frequency.text =  searchMeasurementValue(measurements, "M042").toString() + " " + getString(R.string.herz)
    }
}