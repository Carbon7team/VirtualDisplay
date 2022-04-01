package com.carbon7.virtualdisplay.model

import android.util.Log
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class ProxyUps(addr: String, p: Int, obs: MutableList<Observer> = mutableListOf()): Ups() {
    private var soc = Socket()
    private val ip = addr
    private val port = p
    private lateinit var packet: ByteArray
    override fun requestInfo(s: String) {
        thread {
            try {
                soc.connect(InetSocketAddress(ip,port),5000)
                TODO()
                notify("Packet updated")
            }catch (e: SocketTimeoutException){
                TODO()
            }
        }
    }

    override fun getState(): ByteArray?{
        return packet
        TODO()
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}