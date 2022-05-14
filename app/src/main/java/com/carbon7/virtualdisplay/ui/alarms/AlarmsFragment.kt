package com.carbon7.virtualdisplay.ui.alarms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentAlarmsBinding
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment

class AlarmsFragment : UpsDataVisualizerFragment() {



    override val viewModel: AlarmsViewModel by viewModels()

    private var _binding: FragmentAlarmsBinding? = null
    private val binding
        get() = _binding!!

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.to_botton_anim)}
    private var fabOpened : Boolean = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmsBinding.inflate(inflater, container, false)

        setupView()
        setupVM()

        return binding.root
    }
    private fun setupView(){
        binding.filterAllAlarms.subFab.setOnClickListener {
            viewModel.setCurrentFilter(AlarmsViewModel.Filter.ALL)
            closeFab()
        }
        binding.filterWarningAlarms.subFab.setOnClickListener {
            viewModel.setCurrentFilter(AlarmsViewModel.Filter.WARNING)
            closeFab()
        }
        binding.filterCriticalAlarms.subFab.setOnClickListener {
            viewModel.setCurrentFilter(AlarmsViewModel.Filter.CRITICAL)
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
        binding.listAlarms.adapter = AlarmAdapter(viewModel.filteredAlarms.value ?: listOf())
        viewModel.filteredAlarms.observe(viewLifecycleOwner){
            (binding.listAlarms.adapter as AlarmAdapter).swap(it)
        }


        viewModel.currentFilter.observe(viewLifecycleOwner){
            binding.filterAllAlarms.subFab.isEnabled=true
            binding.filterWarningAlarms.subFab.isEnabled=true
            binding.filterCriticalAlarms.subFab.isEnabled=true

            when(it){
                AlarmsViewModel.Filter.ALL ->       binding.filterAllAlarms.subFab.isEnabled=false
                AlarmsViewModel.Filter.WARNING ->    binding.filterWarningAlarms.subFab.isEnabled=false
                AlarmsViewModel.Filter.CRITICAL ->  binding.filterCriticalAlarms.subFab.isEnabled=false
            }
        }
    }

    private fun openFab(){
        binding.filterAllAlarms.all.visibility = View.VISIBLE
        binding.filterWarningAlarms.all.visibility = View.VISIBLE
        binding.filterCriticalAlarms.all.visibility = View.VISIBLE

        binding.filterAllAlarms.all.startAnimation(fromBottom)
        binding.filterWarningAlarms.all.startAnimation(fromBottom)
        binding.filterCriticalAlarms.all.startAnimation(fromBottom)

        binding.fabMain.startAnimation(rotateOpen)

        fabOpened=true
    }
    private fun closeFab(){
        binding.filterAllAlarms.all.visibility = View.INVISIBLE
        binding.filterWarningAlarms.all.visibility = View.INVISIBLE
        binding.filterCriticalAlarms.all.visibility = View.INVISIBLE

        binding.filterAllAlarms.all.startAnimation(toBottom)
        binding.filterWarningAlarms.all.startAnimation(toBottom)
        binding.filterCriticalAlarms.all.startAnimation(toBottom)

        binding.fabMain.startAnimation(rotateClose)


        fabOpened=false
    }
}