package com.carbon7.virtualdisplay.ui.login

import android.R.attr
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentAlarmsBinding
import androidx.core.app.ActivityCompat.startActivityForResult
import android.provider.Settings;

import android.content.Intent
import android.net.Uri

import android.os.Build
import android.widget.Toast
import com.carbon7.virtualdisplay.MainActivity

import android.R.attr.button
import com.carbon7.virtualdisplay.BuildConfig
import com.carbon7.virtualdisplay.FloatingCallService
import com.carbon7.virtualdisplay.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private val DRAW_OVER_OTHER_APP_PERMISSION = 123
    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        initListeners()

        binding
        binding.btnStartCall.setOnClickListener(View.OnClickListener {
            if (Settings.canDrawOverlays(context)) {
                requireContext().startService(Intent(context, FloatingCallService::class.java))
            } else {
                errorToast()
            }
        })


        return binding.root

    }

    private fun initListeners(){
        binding.btnStartCall.setOnClickListener {
            if(checkHasDrawOverlayPermissions()) {
                requireContext().startService(Intent(context, FloatingCallService::class.java))
            }else{
                navigateDrawPermissionSetting()
            }
        }
    }


    private fun checkHasDrawOverlayPermissions(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    private fun navigateDrawPermissionSetting() {
        val intent1 = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${activity?.packageName}"))
        startActivityForResult(intent1, REQUEST_CODE_DRAW_PREMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_DRAW_PREMISSION){
            checkAndStartService()
        }
    }

    private fun checkAndStartService() {
        if(checkHasDrawOverlayPermissions()) {
            requireContext().startService(Intent(context, FloatingCallService::class.java))
        }
    }

    private fun errorToast() {
        Toast.makeText(context,
            "Draw over other app permission not available. Can't start the application without the permission.",
            Toast.LENGTH_LONG).show()
    }

    companion object{
        const val  ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopfloating.service"
        const val REQUEST_CODE_DRAW_PREMISSION = 2

    }



}