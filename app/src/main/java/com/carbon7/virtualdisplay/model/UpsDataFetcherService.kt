package com.carbon7.virtualdisplay.model

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UpsDataFetcherService: Service() {


    enum class ConnectionState{
        CONNECTED,
        NOT_CONNECTED
    }

    private val binder = LocalBinder()

    private lateinit var ups: Ups
    private val decoder = UpsDataDecoder()
    private lateinit var timer : CountDownTimer
    private var lostPackets = 0
    private var lastConnState: ConnectionState? = null

    /**
     *  EventBus that publish the new data from the UPS
     */
    val dataBus = EventBus<Triple<List<Status>,List<Alarm>,List<Measurement>>>()
    /**
     * EventBus that publish the connection status with UPS
     */
    val connectionStateBus = EventBus<ConnectionState>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.extras != null) {
            val bundle = intent.extras!!
            if (bundle.containsKey("ip") && bundle.containsKey("port")) {
                val ip = bundle.getString("ip")!!
                val port = bundle.getInt("port")

                val interval = bundle.getLong("interval", 1000)
                timer= object: CountDownTimer(Long.MAX_VALUE,interval){
                    override fun onTick(p0: Long) {
                        CoroutineScope(Dispatchers.Default).launch{
                            val result = ups.requestInfo()
                            if(result.isSuccess) {
                                lostPackets=0
                                val data=decoder.decode(result.getOrNull()!!)
                                dataBus.invokeEvent(data)
                                if(lastConnState==null || lastConnState!=ConnectionState.CONNECTED) {
                                    connectionStateBus.invokeEvent(ConnectionState.CONNECTED)
                                    lastConnState=ConnectionState.CONNECTED
                                }
                            }else {
                                lostPackets++
                                if(lostPackets*interval>=2000 && (lastConnState==null || lastConnState!=ConnectionState.NOT_CONNECTED)) {//Se non ricevo pacchetti da 2 secondi
                                    connectionStateBus.invokeEvent(ConnectionState.NOT_CONNECTED)
                                    lastConnState=ConnectionState.NOT_CONNECTED
                                }
                            }
                        }
                    }
                    override fun onFinish() {}
                }

                ups = ProxyUps(ip, port)

                start()
            }else
                stopSelf()
        }else
            stopSelf()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::ups.isInitialized)
            stop()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder(){
        fun getService(): UpsDataFetcherService = this@UpsDataFetcherService
    }

    private fun start(){
        ups.open()
        timer.start()
    }
    private fun stop() {
        timer.cancel()
        ups.close()
    }




}