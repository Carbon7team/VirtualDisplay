package com.carbon7.virtualdisplay.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.databinding.FragmentInputBinding
import com.carbon7.virtualdisplay.model.Measurement
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment
import com.carbon7.virtualdisplay.ui.bypass.InputViewModel

class InputFragment : UpsDataVisualizerFragment() {
    override val viewModel: InputViewModel by viewModels()

    private var _binding: FragmentInputBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater, container, false)

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
        binding.l11.text = searchMeasurementValue(measurements, "==> QUA CI VA L11! <==").toString()
        binding.l12.text = searchMeasurementValue(measurements, "==> QUA CI VA L12! <==").toString()
        binding.l13.text = searchMeasurementValue(measurements, "==> QUA CI VA L13! <==").toString()
        binding.l14.text = searchMeasurementValue(measurements, "==> QUA CI VA L14! <==").toString()
        binding.l21.text = searchMeasurementValue(measurements, "==> QUA CI VA L21! <==").toString()
        binding.l22.text = searchMeasurementValue(measurements, "==> QUA CI VA L22! <==").toString()
        binding.l23.text = searchMeasurementValue(measurements, "==> QUA CI VA L23! <==").toString()
        binding.l24.text = searchMeasurementValue(measurements, "==> QUA CI VA L24! <==").toString()
        binding.l31.text = searchMeasurementValue(measurements, "==> QUA CI VA L31! <==").toString()
        binding.l32.text = searchMeasurementValue(measurements, "==> QUA CI VA L32! <==").toString()
        binding.l33.text = searchMeasurementValue(measurements, "==> QUA CI VA L33! <==").toString()
        binding.l34.text = searchMeasurementValue(measurements, "==> QUA CI VA L34! <==").toString()
        binding.frequency.text = searchMeasurementValue(measurements, "==> QUA CI VA LA FREQUENZA! <==").toString()
    }
}