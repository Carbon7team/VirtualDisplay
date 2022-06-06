package com.carbon7.virtualdisplay

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.os.CountDownTimer
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
import androidx.lifecycle.Observer
import com.carbon7.virtualdisplay.databinding.OverlayCallBinding
import com.carbon7.virtualdisplay.model.*
import com.carbon7.virtualdisplay.ui.call.CallView
import com.carbon7.virtualdisplay.ui.login.LoginFragment.Companion.ACTION_STOP_FOREGROUND
import com.google.gson.*
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Type
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
    private var userId: String? =null
    private var token: String? =null
    private lateinit var cView: CallView


    val ip = "192.168.11.156"


    override fun onBind(intent: Intent?): IBinder? {return null}



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(windowManager == null) {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        }
        if(_binding == null ){
            val li = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _binding = OverlayCallBinding.inflate(li)
        }

        userId = intent?.getStringExtra("userId")
        token = intent?.getStringExtra("token")
        if ((intent?.action != null && intent.action.equals(ACTION_STOP_FOREGROUND, ignoreCase = true)) || userId==null) {
            stopSelf()
        }else {
            generateForegroundNotification()
            addFloatingMenu()

            val initSoc = IO.socket("http://$ip:4000")
            initSoc.connect()
            register(initSoc, userId!!)
            requestCall(initSoc)
            initSoc.on("message"){
                it.forEach { el->
                    Log.d("MyApp",el.toString())
                }
            }
        }


        /*if(uid==null){
            stopSelf()
        }else{
            setupWebView(binding.wvCall, userId)
            binding.wvCall.loadUrl("file:///android_asset/res/call.html")
        }*/




        return START_STICKY
    }

    private lateinit var fetcherService: UpsDataFetcherService
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            fetcherService= (service as UpsDataFetcherService.LocalBinder).getService()
            timer.start()
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            //if the service stop it rebind to it
            bindService(Intent(this@FloatingCallService, UpsDataFetcherService::class.java), this,0)
        }
    }
    private val gson = GsonBuilder()
            .registerTypeAdapter(Status::class.java,object: JsonSerializer<Status>{
                override fun serialize(src: Status, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
                    val json = JsonObject()
                    json.addProperty("code", src.code)
                    json.addProperty("name", getString(src.name))
                    json.addProperty("active", src.isActive)
                    return json
                }
            })
            .registerTypeAdapter(Alarm::class.java,object: JsonSerializer<Alarm>{
                override fun serialize(src: Alarm, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
                    val json = JsonObject()
                    json.addProperty("code", src.code)
                    json.addProperty("name", getString(src.name))
                    json.addProperty("severity",
                        when(src.level){
                            Alarm.Level.CRITICAL -> "Critical"
                            Alarm.Level.WARNING -> "Warning"
                            Alarm.Level.NONE -> "None"
                        }
                    )
                    return json
                }
            })
            .registerTypeAdapter(Measurement::class.java,object: JsonSerializer<Measurement>{
                override fun serialize(src: Measurement, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
                    val json = JsonObject()
                    json.addProperty("code", src.code)
                    json.addProperty("name", src.code)
                    json.addProperty("value",src.value)
                    json.addProperty("unitOfMeasure","")
                    return json
                }
            }).create()



    private val timer= object: CountDownTimer(Long.MAX_VALUE,2000) {
        override fun onTick(p0: Long) {
            val upsData = fetcherService.dataBus.events.value
            var status = JSONArray()
            var alarms = JSONArray()
            var measurements = JSONArray()
            if(upsData!=null) {
                status = JSONArray(gson.toJson(upsData.first))
                alarms = JSONArray(gson.toJson(upsData.second.filter { it.isActive }))
                measurements = JSONArray(gson.toJson(upsData.third))
            }
            val upsConnState =
                if (fetcherService.connectionStateBus.events.value == UpsDataFetcherService.ConnectionState.CONNECTED) "Connected" else "Waiting Connection"

            val data = JSONObject(mapOf(
                "status" to status,
                "alarms" to alarms,
                "measurements" to measurements,
                "uspConnectionState" to upsConnState
            )).toString()
            cView.sendData(data)
        }

        override fun onFinish() {}

    }

    private fun register(soc: Socket, userId:String){
        soc.emit("message",
            JSONObject()
                .put("type","registration")
                .put("idUser", userId)
        )
    }

    private fun requestCall(soc: Socket){
        soc.emit("message",
            JSONObject()
                .put("type","call")
        )
    }

    private suspend fun logout(token: String) = suspendCoroutine<Boolean> { cont ->
            val req = Request.Builder()
                .url("http://$ip:4000/logout")
                .header("Authorization", "Bearer $token")
                .delete()
                .build()
            OkHttpClient().newCall(req).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    cont.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use { res ->
                        if(!res.isSuccessful)
                            cont.resumeWithException(HttpException(res.code(),res.message()))

                        else{
                            cont.resume(true)
                        }
                    }
                }
            })
        }





    override fun onDestroy() {
        super.onDestroy()

        removeFloatingControl()
        stopForeground(true)
        timer.cancel()

        if(::fetcherService.isInitialized)
            unbindService(serviceConnection)
        if(token != null)
            CoroutineScope(Dispatchers.IO).launch { logout(token!!) }


    }


    private fun removeFloatingControl() {
        if(_binding?.root?.parent !=null) {
            windowManager?.removeView(binding.root)
        }
    }

    private fun addFloatingMenu() {
        cView= CallView(this, binding, userId!!, {
            bindService(Intent(this@FloatingCallService, UpsDataFetcherService::class.java), serviceConnection,0)
        },{
            stopSelf()
        })

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