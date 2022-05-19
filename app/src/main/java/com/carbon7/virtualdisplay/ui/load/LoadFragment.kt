package com.carbon7.virtualdisplay.ui.load

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentLoadBinding
import com.carbon7.virtualdisplay.model.Measurement
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment

class LoadFragment : UpsDataVisualizerFragment() {
    override val viewModel: LoadViewModel by viewModels()

    private var _binding: FragmentLoadBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadBinding.inflate(inflater, container, false)

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

    private fun progressBarCalculator(_value: Float?) : Int {
        var value = _value
        if (value != null) {
            value /= 120
            value *= 100
            return value.toInt()
        }
        return 0
    }

    private fun setupView(){
        val measurements = viewModel.filteredMeasurements.value

        binding.l11.text = searchMeasurementValue(measurements, "M039").toString()
        binding.l12.text = searchMeasurementValue(measurements, "M040").toString()
        binding.l13.text = searchMeasurementValue(measurements, "M041").toString()
        binding.l14.text = searchMeasurementValue(measurements, "M043").toString()
        binding.l1Load.text = searchMeasurementValue(measurements, "M043").toString() + " " + getString(R.string.perc)
        binding.l1ProgressBar.progress = progressBarCalculator(searchMeasurementValue(measurements, "M043"))

        binding.l21.text = searchMeasurementValue(measurements, "M044").toString()
        binding.l22.text = searchMeasurementValue(measurements, "M045").toString()
        binding.l23.text = searchMeasurementValue(measurements, "M073").toString()
        binding.l24.text = searchMeasurementValue(measurements, "M074").toString()
        binding.l2Load.text = searchMeasurementValue(measurements, "M043").toString() + " " + getString(R.string.perc)
        binding.l2ProgressBar.progress = progressBarCalculator(searchMeasurementValue(measurements, "M043"))

        binding.l31.text = searchMeasurementValue(measurements, "M075").toString()
        binding.l32.text = searchMeasurementValue(measurements, "M070").toString()
        binding.l33.text = searchMeasurementValue(measurements, "M071").toString()
        binding.l34.text = searchMeasurementValue(measurements, "M072").toString()
        binding.l3Load.text = searchMeasurementValue(measurements, "M043").toString() + " " + getString(R.string.perc)
        binding.l3ProgressBar.progress = progressBarCalculator(searchMeasurementValue(measurements, "M043"))

        binding.lfr1.text = searchMeasurementValue(measurements, "M043").toString()
        binding.lfr2.text = searchMeasurementValue(measurements, "M043").toString()
        binding.lfr4.text = searchMeasurementValue(measurements, "M043").toString()
    }
}