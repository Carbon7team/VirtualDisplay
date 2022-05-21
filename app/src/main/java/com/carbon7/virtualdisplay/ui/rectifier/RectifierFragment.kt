package com.carbon7.virtualdisplay.ui.rectifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentRectifierBinding
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

    @SuppressLint("SetTextI18n")
    private fun setupView(){
        viewModel.measurements.observe(viewLifecycleOwner) {
            binding.l11.text = it[32].value!!.toInt().toString()
            binding.l12.text = it[36].value!!.toInt().toString()
            binding.l13.text = it[67].value!!.toInt().toString()
            binding.l14.text = it[64].value!!.toInt().toString()
            binding.l21.text = it[33].value!!.toInt().toString()
            binding.l22.text = it[37].value!!.toInt().toString()
            binding.l23.text = it[68].value!!.toInt().toString()
            binding.l24.text = it[65].value!!.toInt().toString()
            binding.l31.text = it[34].value!!.toInt().toString()
            binding.l32.text = it[38].value!!.toInt().toString()
            binding.l33.text = it[69].value!!.toInt().toString()
            binding.l34.text = it[66].value!!.toInt().toString()
            binding.frequency.text = it[35].value.toString() + " " + getString(R.string.herz)
        }
    }
}