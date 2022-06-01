package com.carbon7.virtualdisplay

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.carbon7.virtualdisplay.databinding.OverlayCallBinding
import com.carbon7.virtualdisplay.ui.login.LoginFragment.Companion.ACTION_STOP_FOREGROUND
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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

    val ip = "192.168.11.156"

    lateinit var initSoc: Socket
    lateinit var userId : String

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



        CoroutineScope(Dispatchers.IO).launch {
            val pair = login("damiano","password")
            val token = pair.first
            userId  =pair.second
            initSoc = IO.socket("http://$ip:4000")
            initSoc.connect()
            register(initSoc, userId)


            Log.d("MyApp","Precall-1")
            binding.wvCall.post {
                Log.d("MyApp","Precall-2")

                setupWebView(binding.wvCall)
                binding.wvCall.loadUrl("file:///android_asset/res/call.html")


                //binding.wvCall.evaluateJavascript("javascript:call(\"$ip\", \"$userId\")", null)

                call(initSoc)



            }
        }


        return START_STICKY

        //Normal Service To test sample service comment the above    generateForegroundNotification() && return START_STICKY
        // Uncomment below return statement And run the app.
//        return START_NOT_STICKY
    }

    fun call(){

    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun setupWebView(webView: WebView) {

        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        //Create functions that can be called by the javascript code
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun connectionEstablished() {
                Log.d("MyApp", "connectionEstablished")
                //initSoc.close()
                //connStatus=ConnectionStatus.CONNECTED
                //onConnectionEstablished!!.invoke()
            }

            @JavascriptInterface
            fun connectionClosed() {
                Log.d("MyApp", "connectionClosed")
                //connStatus=ConnectionStatus.NOT_CONNECTED
                //onConnectionClosed!!.invoke()
                //onConnectionEstablished = null
                //onConnectionClosed = null
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
    }

    private suspend fun login(username: String, password: String) = suspendCoroutine<Pair<String,String>> { cont ->
        val queue = Volley.newRequestQueue(this)
        val loginReq = JsonObjectRequest(Request.Method.POST, "http://$ip:4000/loginUser",
            JSONObject().put("username", username).put("password", password),
            {
                Log.d("MyApp", it.toString(4))
                cont.resume(Pair(it.getString("token"),it.getString("user")))
            },{
                Log.d("MyApp", it.toString())

                cont.resumeWithException(it.cause ?: Exception("Errore durante login"))
            }
        )
        queue.add(loginReq)
    }

    private fun register(soc:Socket,userId:String){

        soc.emit("message",
            JSONObject()
                .put("type","registration")
                .put("idUser", userId)
        )
    }

    private fun call(soc:Socket){
        soc.emit("message",
            JSONObject()
                .put("type","call")
        )
    }


    /*fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                Log.d("WebView", "PERMISSION FOR AUDIO")
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    myRequest.grant(myRequest.getResources())
                    mBinding.webView.loadUrl("<your url>")
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    fun askForPermission(origin: String, permission: String, requestCode: Int) {
        Log.d("WebView", "inside askForPermission for" + origin + "with" + permission)
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission
                )
            ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(permission),
                    requestCode
                )
            }
        } else {
            myRequest.grant(myRequest.getResources())
        }
    }*/









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
            params.height = dpToPx(50)
            params.width = dpToPx(50)
            iconWidth = params.width
            iconHeight = params.height
            screenHeight = windowManager?.defaultDisplay?.height ?: 0
            screenWidth = windowManager?.defaultDisplay?.width ?: 0
            //Initial position of the floating controls
            params.gravity = Gravity.TOP or Gravity.START
            params.x = 0
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

        binding.button.setOnClickListener {
            Log.d("TOUCH","Button - CLICKED")
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