package com.carbon7.virtualdisplay.ui.call

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.carbon7.virtualdisplay.BuildConfig
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.OverlayCallBinding

@SuppressLint("ClickableViewAccessibility")
class CallView(val context: Context, val binding:OverlayCallBinding, val userId: String, val onCallStarted: ()->Unit, val onCallEnded: ()->Unit) {

    //private var _binding: OverlayCallBinding? = null
    //private val binding
    //    get() = _binding!!


    private var callAnswered=false


    private val overlayShowDetail: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.overlay_show_details)}
    private val overlayHideDetail: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.overlay_hide_details)}
    private val micOn : Drawable by lazy { AppCompatResources.getDrawable(context, R.drawable.ic_baseline_mic_off_24)!!}
    private val micOff : Drawable by lazy { AppCompatResources.getDrawable(context, R.drawable.ic_baseline_mic_off_24)!!}
    private val phoneTalking : Drawable by lazy { AppCompatResources.getDrawable(context, R.drawable.ic_baseline_phone_in_talk_24)!!}

    init{
        setupTouchListener()
        setupWebView(binding.wvCall, userId)
        binding.wvCall.loadUrl("file:///android_asset/res/call.html")

    }


    private fun setupTouchListener(){

        //TODO handle changing the position of overlay view

        binding.btnCall.setOnTouchListener{view, motionEvent ->
            Log.d("TOUCH", "Button - CLICKED")
            if(motionEvent.action == MotionEvent.ACTION_DOWN) {
                setDetailVisibility(binding.dettagli.visibility == View.GONE)
            }
            return@setOnTouchListener true
        }

        var muted=false
        binding.toggleMicrphone.setOnClickListener{
            if(muted) {
                binding.wvCall.evaluateJavascript("javascript:unmute()", null)
                binding.imgToggleMicrophone.setImageDrawable(micOff)
                binding.txtToggleMicrophone.text = "Mute"
            }else {
                binding.wvCall.evaluateJavascript("javascript:mute()", null)
                binding.imgToggleMicrophone.setImageDrawable(micOn)
                binding.txtToggleMicrophone.text = "Unmute"
            }
            muted=!muted
        }

        binding.endCall.setOnClickListener{
            Log.d("MyApp","END_CALL")
            binding.wvCall.evaluateJavascript("javascript:endCall()", null)
        }

        binding.layout.setOnTouchListener { view, motionEvent ->
            false
        }
    }


    /**
     * It show or hide the detail cardView with some cool animation
     */
    private fun setDetailVisibility(show: Boolean){
        if(show && binding.dettagli.visibility==View.GONE){
            binding.btnCall.layoutParams =
                (binding.btnCall.layoutParams as ConstraintLayout.LayoutParams).apply {
                    marginStart = dpToPx(64)
                }
            binding.btnCall.startAnimation(overlayShowDetail)
            binding.btnCallBadge.startAnimation(overlayShowDetail)
            binding.dettagli.visibility = View.VISIBLE
        } else if(binding.dettagli.visibility==View.VISIBLE){
            binding.btnCall.layoutParams =
                (binding.btnCall.layoutParams as ConstraintLayout.LayoutParams).apply {
                    marginStart = dpToPx(0)
                }
            binding.btnCall.startAnimation(overlayHideDetail)
            binding.btnCallBadge.startAnimation(overlayHideDetail)
            binding.dettagli.visibility = View.GONE
        }
    }



    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun setupWebView(webView: WebView, userId: String) {

        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        //Create functions that can be called by the javascript code
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun connectionEstablished() {
                Log.d("MyApp", "connectionEstablished")
                //bindService(Intent(this@FloatingCallService, UpsDataFetcherService::class.java), serviceConnection,0)

            }

            @JavascriptInterface
            fun connectionClosed() {
                Log.d("MyApp", "connectionClosed")
                onCallEnded()
            }

            @JavascriptInterface
            fun callStarted() {
                Log.d("MyApp", "callStarted")
                callAnswered=true
                binding.btnCallBadge.setImageDrawable(phoneTalking)
                binding.dettagli.post { setDetailVisibility(true) }
                onCallStarted()
            }

            @JavascriptInterface
            fun callEnded() {
                Log.d("MyApp", "callEnded")
                //NON FUNZIONA
            }
        }, "App")

        //When the webpage (not blank) is loaded it start the script
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("MyApp","Page loaded")
                if(url != "about:blank")
                    webView.post { webView.evaluateJavascript("javascript:call(\"${BuildConfig.SERVER_ADDRESS}\", \"$userId\")", null) }
            }
        }
        //Grant the permissions that the webview request (microphone)
        webView.webChromeClient = object : WebChromeClient(){
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        }
    }

    /**
     * Send data to remote support
     * @param   data String of data to be sent
     */
    fun sendData(data: String){
        binding.wvCall.post { binding.wvCall.evaluateJavascript("javascript:sendData('$data')", null) }
    }


    private fun dpToPx(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }






}