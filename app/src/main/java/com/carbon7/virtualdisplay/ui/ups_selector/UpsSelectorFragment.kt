package com.carbon7.virtualdisplay.ui.ups_selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carbon7.virtualdisplay.databinding.FragmentUpsSelectorBinding
import com.carbon7.virtualdisplay.databinding.ListItemUpsBinding
import com.carbon7.virtualdisplay.model.AppDB
import android.widget.Button


// chiamare metodo in view model quando viene fatto un interazione
// osservare live data view model e adattare la grafica

class UpsSelectorFragment : Fragment() {

    companion object {
        fun newInstance() = UpsSelectorFragment()
    }

    private lateinit var viewModel: UpsSelectorViewModel
    private var _binding: FragmentUpsSelectorBinding? = null
    private var _bindingUps: ListItemUpsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bindingUps
        get() = _bindingUps!!

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
        _bindingUps = ListItemUpsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.fabAdd.setOnClickListener {
            CreateNewUpsDialog { name: String, ip: String, port: Int ->
                viewModel.addUps(name, ip, port)
            }.show(parentFragmentManager, "Create UPS")
        }

        bindingUps.btnUpsModify.setOnClickListener {
            ModifyUpsDialog { id: Int, name: String, ip: String, port: Int ->
                viewModel.modifyUps(id, name, ip, port)
            }.show(parentFragmentManager, "Modify UPS")
        }

        bindingUps.btnUpsDelete.setOnClickListener {
            bindingUps.ups
            viewModel.deleteUps(id)
        }

        // val d2 = CreateNewUpsDialog(viewModel::addUps) // funziona uguale ma viene passato direttamente la funzione

        return root
    }
}