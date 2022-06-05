package com.carbon7.virtualdisplay

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import com.carbon7.virtualdisplay.databinding.OverlayCallBinding
import com.carbon7.virtualdisplay.ui.login.LoginFragment.Companion.ACTION_STOP_FOREGROUND
import com.google.android.material.badge.BadgeDrawable
import kotlinx.coroutines.*


class FloatingCallService: Service() {
    private var windowManager: WindowManager? = null
    //private var floatingControlView: ViewGroup? = null

    private var _binding: OverlayCallBinding? = null
    private val binding
        get() = _binding!!

    var iconHeight = 0
    var iconWidth = 0
    private var screenHeight = 0
    private var screenWidth = 0
    private var hideHandler: Handler? = null
    private var hideRunnable: Runnable? = null

    private val overlayShowDetail: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.overlay_show_details)}
    private val overlayHideDetail: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.overlay_hide_details)}

    private var callAnswered=false

    val ip = "192.168.11.156"


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(windowManager == null) {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        }
        if(_binding == null ){
            val li = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _binding = OverlayCallBinding.inflate(li/*null, false*/)
            //floatingControlView = li.inflate(R.layout.overlay_call, null) as ViewGroup?
        }

        if (intent?.action != null && intent.action.equals(ACTION_STOP_FOREGROUND, ignoreCase = true)) {
            removeFloatingControl()
            stopForeground(true)
            stopSelf()
        }else {
            generateForegroundNotification()
            addFloatingMenu()
        }


        val userId = intent?.getStringExtra("userId")
        if(userId==null){
            removeFloatingControl()
            stopForeground(true)
            stopSelf()
        }else{
            setupWebView(binding.wvCall, userId)
            binding.wvCall.loadUrl("file:///android_asset/res/call.html")
        }


        return START_STICKY

        //Normal Service To test sample service comment the above    generateForegroundNotification() && return START_STICKY
        // Uncomment below return statement And run the app.
//        return START_NOT_STICKY
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
            }

            @JavascriptInterface
            fun connectionClosed() {
                Log.d("MyApp", "connectionClosed")
                removeFloatingControl()
                stopForeground(true)
                stopSelf()
            }

            @JavascriptInterface
            fun callStarted() {
                Log.d("MyApp", "callStarted")
                callAnswered=true
                binding.btnCallBadge.setImageDrawable(AppCompatResources.getDrawable(this@FloatingCallService, R.drawable.ic_baseline_phone_in_talk_24))
                binding.dettagli.post {
                    toggleDetails(true)
                }
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
                    webView.post { webView.evaluateJavascript("javascript:call(\"$ip\", \"$userId\")", null) }
            }
        }
        webView.webChromeClient = object : WebChromeClient(){
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        }
    }





    private fun removeFloatingControl() {
        if(_binding?.root?.parent !=null) {
            windowManager?.removeView(binding.root)
        }
    }

    private fun addFloatingMenu() {
        if (_binding?.root?.parent == null) {
            //Set layout params to display the controls over any screen.
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_PHONE else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            //params.height = dpToPx(256)
            //params.width = dpToPx(256)

            iconWidth = params.width
            iconHeight = params.height
            screenHeight = windowManager?.defaultDisplay?.height ?: 0
            screenWidth = windowManager?.defaultDisplay?.width ?: 0
            //Initial position of the floating controls
            params.gravity = Gravity.TOP or Gravity.START
            params.x = 10
            params.y = 100

            //Add the view to window manager
            windowManager?.addView(binding.root, params)
            try {
                addOnTouchListener(params)
            } catch (e: Exception) {
                // TODO: handle exception
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addOnTouchListener(params: WindowManager.LayoutParams) {
        //Add touch listerner to floating controls view to move/close/expand the controls

        binding.btnCall.setOnTouchListener{view, motionEvent ->
            if(!callAnswered)
                return@setOnTouchListener false
            Log.d("TOUCH", "Button - CLICKED")
            if(motionEvent.action == MotionEvent.ACTION_DOWN) {
                toggleDetails(binding.dettagli.visibility == View.GONE)
            }
            return@setOnTouchListener true

        }
        binding.speakerphone.setOnClickListener{
            Log.d("MyApp","SPEAKERPHONE")
        }
        var muted=false
        binding.toggleMicrphone.setOnClickListener{
            if(muted) {
                binding.wvCall.evaluateJavascript("javascript:unmute()", null)
                binding.imgToggleMicrophone.setImageDrawable(
                    AppCompatResources.getDrawable(this, R.drawable.ic_baseline_mic_off_24)
                )
                binding.txtToggleMicrophone.text = "Mute"
            }else {
                binding.wvCall.evaluateJavascript("javascript:mute()", null)
                binding.imgToggleMicrophone.setImageDrawable(
                    AppCompatResources.getDrawable(this, R.drawable.ic_baseline_mic_24)
                )
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
       /*binding.button.setOnTouchListener ( object : View.OnTouchListener {
            private var touchPressed :Long = System.currentTimeMillis()
            private var moved=false
            override fun onTouch(view: View?, me: MotionEvent): Boolean {
                when(me.action){
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("TOUCH","Button - ACTION_DOWN")
                        moved=false
                        Log.d("TOUCH", "Button - dt? ${me.downTime}")
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("TOUCH","Button - ACTION_UP")
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.d("TOUCH","Button - ACTION_MOVE")
                        moved=true
                        return false
                    }
                    else ->{
                        return false
                    }
                }
            }
        })

        binding.root.setOnTouchListener(object : View.OnTouchListener {
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(view: View?, motionevent: MotionEvent): Boolean {
                when (motionevent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("TOUCH","Root - ACTION_DOWN")
                        params.alpha = 1.0f
                        initialTouchX = motionevent.rawX
                        initialTouchY = motionevent.rawY
                        Log.d("OnTouchListener",
                            java.lang.StringBuilder("POS: x = ").append(params.x)
                                .append(" y = ").append(params.y)
                                .append(" tx = ").append(initialTouchX)
                                .append(" ty = ").append(initialTouchY).toString()
                        )
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("TOUCH","Root - ACTION_UP")
                        /*return when {
                            abs(initialTouchX - motionevent.rawX) >= 25f -> {
                                true
                            }
                            abs(initialTouchY - motionevent.rawY) >= 25f -> {
                                true
                            }
                            else -> {
                                true
                            }
                        }*/
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.d("TOUCH","Root - ACTION_MOVE")
                        params.x = (motionevent.rawX - (iconWidth / 2).toFloat()).toInt()
                        params.y = (motionevent.rawY - iconHeight.toFloat()).toInt()
                        try {
                            windowManager?.updateViewLayout(binding.root, params)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        return true
                    }
                    else -> {
                        return false
                    }
                }
            }
        })*/


    }

    private fun toggleDetails(show: Boolean){
        if(show){
            binding.btnCall.layoutParams =
                (binding.btnCall.layoutParams as ConstraintLayout.LayoutParams).apply {
                    marginStart = dpToPx(64)
                }
            binding.btnCall.startAnimation(overlayShowDetail)
            binding.btnCallBadge.startAnimation(overlayShowDetail)
            binding.dettagli.visibility = View.VISIBLE
        } else {
            binding.btnCall.layoutParams =
                (binding.btnCall.layoutParams as ConstraintLayout.LayoutParams).apply {
                    marginStart = dpToPx(0)
                }
            binding.btnCall.startAnimation(overlayHideDetail)
            binding.btnCallBadge.startAnimation(overlayHideDetail)
            binding.dettagli.visibility = View.GONE
        }
    }


    //Notififcation for ON-going
    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    private fun generateForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intentMainLanding, 0)
            iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            if (mNotificationManager == null) {
                mNotificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannelGroup(
                    NotificationChannelGroup("chats_group", "Chats")
                )
                val notificationChannel =
                    NotificationChannel("service_channel", "Service Notifications",
                        NotificationManager.IMPORTANCE_MIN)
                notificationChannel.enableLights(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append(" service is running").toString())
                .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
                .setContentText("Touch to open") //                    , swipe down for more options.
                .setSmallIcon(R.drawable.ic_delete)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
            }
            builder.color = resources.getColor(R.color.purple_200)
            notification = builder.build()
            startForeground(mNotificationId, notification)
        }

    }

    //Method to convert dp to px
    private fun dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

}