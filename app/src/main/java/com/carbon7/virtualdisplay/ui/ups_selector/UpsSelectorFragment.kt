package com.carbon7.virtualdisplay.ui.ups_selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carbon7.virtualdisplay.databinding.FragmentUpsSelectorBinding
import com.carbon7.virtualdisplay.model.ProxyUps
import com.carbon7.virtualdisplay.model.UpsData

class UpsSelectorFragment : Fragment() {

    companion object {
        fun newInstance() = UpsSelectorFragment()
    }

    private lateinit var viewModel: UpsSelectorViewModel
    private var _binding: FragmentUpsSelectorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(UpsSelectorViewModel::class.java)

        viewModel.filteredStatus.observe(viewLifecycleOwner){
            val recyclerViewState = binding.upsSelector.layoutManager?.onSaveInstanceState()
            binding.upsSelector.adapter = UpsSelectorAdapter(it)
            binding.upsSelector.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }

        _binding = FragmentUpsSelectorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.load(UpsData(ProxyUps("192.168.137.1",8888)))

        return root
    }
}