package com.carbon7.virtualdisplay.ui.diagram

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentDiagramBinding
import com.carbon7.virtualdisplay.databinding.FragmentStatusBinding
import com.carbon7.virtualdisplay.ui.status.StatusViewModel

class DiagramFragment : Fragment() {

    companion object {
        fun newInstance() = DiagramFragment()
    }

    private lateinit var viewModel: DiagramViewModel
    private var _binding: FragmentDiagramBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DiagramViewModel::class.java)

        _binding = FragmentDiagramBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


}