package com.carbon7.virtualdisplay.ui.status

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentStatusBinding
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.model.UpsDataFetcherService
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment

class StatusFragment : UpsDataVisualizerFragment() {

    override val viewModel: StatusViewModel by viewModels()

    private var _binding: FragmentStatusBinding? = null
    private val binding
        get() = _binding!!


    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.to_botton_anim)}
    private var fabOpened : Boolean = false;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)

        setupView()
        setupVM()

        return binding.root
    }

    private fun setupView(){
        binding.filterAllStatus.subFab.setOnClickListener {
            viewModel.setCurrentFilter(StatusViewModel.Filter.ALL)
            closeFab()
        }
        binding.filterActiveStatus.subFab.setOnClickListener {
            viewModel.setCurrentFilter(StatusViewModel.Filter.ACTIVE)
            closeFab()
        }
        binding.filterInactiveStatus.subFab.setOnClickListener {
            viewModel.setCurrentFilter(StatusViewModel.Filter.INACTIVE)
            closeFab()
        }

        binding.fabMain.setOnClickListener{
            if(!fabOpened)
                openFab()
            else
                closeFab()
        }
    }
    private fun setupVM(){
        binding.listStatus.adapter = StatusAdapter(viewModel.filteredStatus.value ?: listOf())
        viewModel.filteredStatus.observe(viewLifecycleOwner){
            (binding.listStatus.adapter as StatusAdapter).swap(it)
        }


        viewModel.currentFilter.observe(viewLifecycleOwner){
            binding.filterAllStatus.subFab.isEnabled=true
            binding.filterActiveStatus.subFab.isEnabled=true
            binding.filterInactiveStatus.subFab.isEnabled=true

            when(it){
                StatusViewModel.Filter.ALL ->       binding.filterAllStatus.subFab.isEnabled=false
                StatusViewModel.Filter.ACTIVE ->    binding.filterActiveStatus.subFab.isEnabled=false
                StatusViewModel.Filter.INACTIVE ->  binding.filterInactiveStatus.subFab.isEnabled=false
            }
        }
    }

    private fun openFab(){
        binding.filterAllStatus.all.visibility = View.VISIBLE
        binding.filterActiveStatus.all.visibility = View.VISIBLE
        binding.filterInactiveStatus.all.visibility = View.VISIBLE

        binding.filterAllStatus.all.startAnimation(fromBottom)
        binding.filterActiveStatus.all.startAnimation(fromBottom)
        binding.filterInactiveStatus.all.startAnimation(fromBottom)

        binding.fabMain.startAnimation(rotateOpen)

        fabOpened=true
    }
    private fun closeFab(){
        binding.filterAllStatus.all.visibility = View.INVISIBLE
        binding.filterActiveStatus.all.visibility = View.INVISIBLE
        binding.filterInactiveStatus.all.visibility = View.INVISIBLE

        binding.filterAllStatus.all.startAnimation(toBottom)
        binding.filterActiveStatus.all.startAnimation(toBottom)
        binding.filterInactiveStatus.all.startAnimation(toBottom)

        binding.fabMain.startAnimation(rotateClose)


        fabOpened=false
    }
}