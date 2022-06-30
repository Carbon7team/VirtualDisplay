package com.carbon7.virtualdisplay.ui.login

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import android.provider.Settings

import android.content.Intent
import android.net.Uri

import android.widget.Toast

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.carbon7.virtualdisplay.BuildConfig
import com.carbon7.virtualdisplay.FloatingCallService
import com.carbon7.virtualdisplay.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private val DRAW_OVER_OTHER_APP_PERMISSION = 123
    private val REQUEST_MICROPHONE = 124
    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    var userId: String? = null
    var token :String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //TODO effettuare logout se si esce dalla vistaenza aver effettuato la chiamata

        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        binding.btnLogin.setOnClickListener{
                viewModel.login(binding.txtUsername.text.toString(), binding.txtPassword.text.toString()) {uid, tok ->
                    userId = uid
                    token = tok
                    binding.btnStartCall.isEnabled=true
                    binding.btnLogin.isEnabled=false
                    reqMic()
                    if(!checkHasDrawOverlayPermissions())
                        navigateDrawPermissionSetting()
            }
        }
        binding.btnStartCall.setOnClickListener{
            if(checkMic()) {
                if (userId != null && token != null) {
                    if (Settings.canDrawOverlays(context)) {
                        requireContext().startService(
                            Intent(context, FloatingCallService::class.java)
                                .putExtra("userId", userId)
                                .putExtra("token", token)
                        )
                        requireFragmentManager().popBackStack()
                    } else {
                        errorToast()
                    }
                }
            }else
                reqMic()

        }

        viewModel.loginMsg.observe(viewLifecycleOwner){
            val (msg, type) = it
            binding.lblResponse.text= msg
            binding.lblResponse.setTextColor(if(type) Color.RED else Color.GREEN)
        }

        return binding.root

    }


    /**
     * If not granted request to the user the microphone permission
     */
    private fun reqMic(){
        if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_MICROPHONE
            )
        }
    }

    /**
     * Check if microphone permission is granted
     */
    private fun checkMic(): Boolean{
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
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