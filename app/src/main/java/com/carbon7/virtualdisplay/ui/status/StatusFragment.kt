package com.carbon7.virtualdisplay.ui.status

import android.app.DatePickerDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentStatusBinding
import com.carbon7.virtualdisplay.model.ProxyUps
import com.carbon7.virtualdisplay.model.UpsData
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.room.Room
import com.carbon7.virtualdisplay.model.UpsDataService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {


    companion object {
        fun newInstance() = StatusFragment()
    }

    private lateinit var mService: UpsDataService
    private val connection = object : ServiceConnection{
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d("MyApp", "SERVIZIO CONNESSO")
            mService= (service as UpsDataService.LocalBinder).getService()
            viewModel.load(mService.eventBus)

        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d("MyApp", "SERVIZIO DISCONNESSO")
        }
    }

    private val viewModel: StatusViewModel by viewModels()


    private var _binding: FragmentStatusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!


    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.to_botton_anim)}
    private var fabOpened : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyApp","StatusFragment Created")

        requireContext().bindService(
            Intent(requireContext(), UpsDataService::class.java), connection,0
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("MyApp","StatusFragment ViewCreated")


        _binding = FragmentStatusBinding.inflate(inflater, container, false)


        setupView()
        setupVM()



        //viewModel.load(UpsData(ProxyUps("192.168.11.178",8888)))

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
        viewModel.filteredStatus.observe(viewLifecycleOwner){
            val recyclerViewState = binding.listStatus.layoutManager?.onSaveInstanceState()
            binding.listStatus.adapter = StatusAdapter(it)
            binding.listStatus.layoutManager?.onRestoreInstanceState(recyclerViewState)
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


    override fun onDestroy() {
        super.onDestroy()
        //requireContext().unbindService(connection)
        Log.d("MyApp","StatusFragment Destroyed")
    }
    override fun onStop() {
        super.onStop()
        Log.d("MyApp","StatusFragment Stopped")
    }
    override fun onPause() {
        super.onPause()
        Log.d("MyApp","StatusFragment Paused")
        requireContext().unbindService(connection)
        viewModel.unload()
    }
    override fun onResume() {
        super.onResume()
        Log.d("MyApp","StatusFragment Resumed")
        requireContext().bindService(
            Intent(requireContext(), UpsDataService::class.java), connection,0
        )

    }
    override fun onStart() {
        super.onStart()
        Log.d("MyApp","StatusFragment Started")
    }

}