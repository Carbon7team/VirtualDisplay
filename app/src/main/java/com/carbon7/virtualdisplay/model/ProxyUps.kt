package com.carbon7.virtualdisplay.model

import android.util.Log
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class ProxyUps(ip: String, port: Int, obs: MutableList<Observer> = mutableListOf()): Ups() {
    private var packet: ByteArray? = null
    private var mc: ModbusConnection = ModbusConnection(ip, port, onConnection = { mbc->
            TODO()
        },onClose = { err->
            TODO()
        })

    override fun requestInfo(req: String) {
        packet = mc.sendData(req)
        notify("Packet updated")
    }

    override fun getState(): ByteArray?{
        return packet
        TODO()
    }

    override fun close() {
        mc.close()
        TODO("Not yet implemented")
    }
}