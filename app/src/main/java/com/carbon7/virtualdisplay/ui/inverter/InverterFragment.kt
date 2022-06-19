package com.carbon7.virtualdisplay.ui.inverter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentInverterBinding
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

    @SuppressLint("ResourceAsColor")
    private fun colorProgressBar(progress : Int, progressBar : ProgressBar) {
        when {
            progress < 31 -> {
                progressBar.progressTintList = ColorStateList.valueOf(Color.GREEN)
            }
            progress < 61 -> {
                progressBar.progressTintList= ColorStateList.valueOf(Color.YELLOW)
            }
            else -> {
                progressBar.progressTintList = ColorStateList.valueOf(Color.RED)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(){
        viewModel.measurements.observe(viewLifecycleOwner){
            binding.l11.text = it[54].value!!.toInt().toString()
            binding.l12.text = it[10].value!!.toInt().toString()
            binding.l21.text = it[55].value!!.toInt().toString()
            binding.l22.text = it[22].value!!.toInt().toString()
            binding.l31.text = it[56].value!!.toInt().toString()
            binding.l32.text = it[12].value!!.toInt().toString()
            binding.frequency.text = it[13].value!!.toString() + " " + getString(R.string.herz)
            binding.temperature.text = getString(R.string.t_amb) + "\n" + it[15].value!!.toString() + " " + getString(R.string.celsius)
            val progressBar = binding.temperatureScrollBar
            progressBar.progress = it[15].value!!.toInt()
            colorProgressBar(progressBar.progress, progressBar)
        }
    }
}