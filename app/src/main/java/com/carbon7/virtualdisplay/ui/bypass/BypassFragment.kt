package com.carbon7.virtualdisplay.ui.bypass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carbon7.virtualdisplay.databinding.FragmentBypassBinding
import com.carbon7.virtualdisplay.ui.diagram.DiagramFragment

class BypassFragment : Fragment() {

    companion object {
        fun newInstance() = DiagramFragment()
    }

    private lateinit var viewModel: BypassViewModel
    private var _binding: FragmentBypassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BypassViewModel::class.java)

        _binding = FragmentBypassBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
}