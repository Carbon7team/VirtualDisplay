package com.carbon7.virtualdisplay.ui.bypass

import android.annotation.SuppressLint
import android.os.Bundle
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

    @SuppressLint("SetTextI18n")
    private fun setupView(){
        viewModel.measurements.observe(viewLifecycleOwner) {
            binding.l11.text = it[39].value!!.toInt().toString()
            binding.l12.text = it[43].value!!.toInt().toString()
            binding.l13.text = it[73].value!!.toInt().toString()
            binding.l14.text = it[70].value!!.toInt().toString()
            binding.l21.text = it[40].value!!.toInt().toString()
            binding.l22.text = it[44].value!!.toInt().toString()
            binding.l23.text = it[74].value!!.toInt().toString()
            binding.l24.text = it[71].value!!.toInt().toString()
            binding.l31.text = it[41].value!!.toInt().toString()
            binding.l32.text = it[45].value!!.toInt().toString()
            binding.l33.text = it[75].value!!.toInt().toString()
            binding.l34.text = it[72].value!!.toInt().toString()
            binding.frequency.text = it[42].value!!.toString() + " " + getString(R.string.herz)
        }
    }
}