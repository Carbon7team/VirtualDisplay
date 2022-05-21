package com.carbon7.virtualdisplay.ui.load

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentLoadBinding
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

    @SuppressLint("SetTextI18n")
    private fun setupView(){
        viewModel.measurements.observe(viewLifecycleOwner){
            binding.l11.text = it[48].value.toString()
            binding.l12.text = it[51].value.toString()
            binding.l13.text = it[6].value.toString()
            binding.l14.text = it[1].value!!.toInt().toString()
            binding.l1Load.text = it[1].value!!.toInt().toString() + " " + getString(R.string.perc)
            binding.l21.text = it[49].value.toString()
            binding.l22.text = it[52].value.toString()
            binding.l23.text = it[7].value.toString()
            binding.l24.text = it[2].value!!.toInt().toString()
            binding.l2Load.text = it[2].value!!.toInt().toString() + " " + getString(R.string.perc)
            binding.l31.text = it[50].value.toString()
            binding.l32.text = it[53].value.toString()
            binding.l33.text = it[8].value.toString()
            binding.l34.text = it[3].value!!.toInt().toString()
            binding.l3Load.text = it[3].value!!.toInt().toString() + " " + getString(R.string.perc)
            binding.lfr1.text = it[4].value.toString()
            binding.lfr2.text = it[5].value.toString()
            binding.lfr4.text = it[0].value!!.toInt().toString()
            binding.l1ProgressBar.progress = ((it[1].value!! / 120) * 100).toInt()
            binding.l2ProgressBar.progress = ((it[2].value!! / 120) * 100).toInt()
            binding.l3ProgressBar.progress = ((it[3].value!! / 120) * 100).toInt()
        }
    }
}