package com.carbon7.virtualdisplay.ui.ups_selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carbon7.virtualdisplay.databinding.FragmentUpsSelectorBinding
import com.carbon7.virtualdisplay.model.AppDB

// chiamare metodo in view model quando viene fatto un interazione
// osservare live data view model e adattare la grafica

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
        val dao = AppDB.getInstance(requireContext()).dao

        viewModel = ViewModelProvider(this).get(UpsSelectorViewModel::class.java)

        viewModel.ups.observe(viewLifecycleOwner) {
            val recyclerViewState = binding.upsList.layoutManager?.onSaveInstanceState()
            binding.upsList.adapter = UpsSelectorAdapter(it)
            binding.upsList.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }

        viewModel.load(dao)

        _binding = FragmentUpsSelectorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAdd.setOnClickListener {
            CreateNewUpsDialog { name: String, ip: String, port: Int ->
                viewModel.addUps(name, ip, port)
            }.show(parentFragmentManager, "Create UPS")
        }

        // val d2 = CreateNewUpsDialog(viewModel::addUps) // funziona uguale ma viene passato direttamente la funzione

        return root
    }
}