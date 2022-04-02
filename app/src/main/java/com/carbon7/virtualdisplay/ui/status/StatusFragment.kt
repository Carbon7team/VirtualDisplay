package com.carbon7.virtualdisplay.ui.status

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentStatusBinding
import com.carbon7.virtualdisplay.model.Status

class StatusFragment : Fragment() {

    companion object {
        fun newInstance() = StatusFragment()
    }

    private lateinit var viewModel: StatusViewModel
    private var _binding: FragmentStatusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StatusViewModel::class.java)

        viewModel.status.observe(viewLifecycleOwner,{
            Log.d("MyApp","UPDATE")
            binding.listStatus.adapter = StatusAdapter(requireContext(),it)
        })




        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btn1.setOnClickListener { viewModel.addS000(true)}
        binding.btn2.setOnClickListener { viewModel.addS001(true)}
        binding.btn3.setOnClickListener { viewModel.addS002(true)}
        binding.btn4.setOnClickListener { viewModel.addS000(false)}
        binding.btn5.setOnClickListener { viewModel.addS001(false)}
        binding.btn6.setOnClickListener { viewModel.addS002(false)}



        return root
    }

}