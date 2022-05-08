package com.carbon7.virtualdisplay.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import com.carbon7.virtualdisplay.model.UpsDataFetcherService

abstract class UpsDataVisualizerFragment: Fragment() {

    //view is defined in the subclass
    protected abstract val viewModel : UpsDataVisualizerViewModel
    private lateinit var fetcherService: UpsDataFetcherService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            fetcherService= (service as UpsDataFetcherService.LocalBinder).getService()
            viewModel.bind(fetcherService.dataBus)
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            //if the service stop it rebind to it
            requireContext().bindService(
                Intent(requireContext(), UpsDataFetcherService::class.java), this,0
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().bindService(
            Intent(requireContext(), UpsDataFetcherService::class.java), connection,0
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unbindService(connection)
    }


}