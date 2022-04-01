package com.carbon7.virtualdisplay.ui.alarms

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentAlarmsBinding
import com.carbon7.virtualdisplay.databinding.FragmentDiagramBinding
import com.carbon7.virtualdisplay.databinding.FragmentStatusBinding
import com.carbon7.virtualdisplay.ui.status.StatusViewModel

class AlarmsFragment : Fragment() {

    companion object {
        fun newInstance() = AlarmsFragment()
    }

    private lateinit var viewModel: AlarmsViewModel
    private var _binding: FragmentAlarmsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(AlarmsViewModel::class.java)

        _binding = FragmentAlarmsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

}