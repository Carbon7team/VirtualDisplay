package com.carbon7.virtualdisplay.ui.ups_selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carbon7.virtualdisplay.databinding.FragmentUpsSelectorBinding
import com.carbon7.virtualdisplay.model.AppDB

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
        _binding = FragmentUpsSelectorBinding.inflate(inflater, container, false)

        binding.upsList.adapter = UpsSelectorAdapter(viewModel.ups.value?: listOf(),
            onDelete = {
                DeleteUpsDialog(it.name) {
                    viewModel.deleteUps(it.ID)
                }.show(parentFragmentManager, "Delete UPS")
        },
            onModify = {
                ModifyUpsDialog(it.name, it.address, it.port) { name: String, ip: String, port: Int ->
                    viewModel.modifyUps(it.ID, name, ip, port)
                }.show(parentFragmentManager, "Modify UPS")
        },
            onSelected = {
                // TODO FAR PARTIRE MAINACTIVITY MANDANDO IP E PORTA
            })

        viewModel.ups.observe(viewLifecycleOwner) {
            (binding.upsList.adapter as UpsSelectorAdapter).swap(it)
        }

        viewModel.load(dao)

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