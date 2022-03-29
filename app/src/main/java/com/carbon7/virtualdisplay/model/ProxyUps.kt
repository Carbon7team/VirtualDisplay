package com.carbon7.virtualdisplay.model

import android.util.Log
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class ProxyUps(ip: String, port: Int, obs: MutableList<Observer> = mutableListOf()): Ups() {
    private var packet: ByteArray? = null
    private var soc: Socket

    companion object{
        private val ipv4_pattern=
            ("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$").toRegex()
    }

    init {
        if(!ipv4_pattern.matches(ip))
            throw IllegalArgumentException("Indirizzo ip non valido")
        if(!(port in 0..35565))//port not in 0..35565
            throw IllegalArgumentException("Porta non valida")
        soc = Socket()
        thread {
            try {
                soc.connect(InetSocketAddress(ip,port),5000)
            }catch (e:SocketTimeoutException){
                Log.d("myApp", e.toString())
            }
        }
    }

    override fun close(){
        soc.close() //CRASHA SE soc NON E' INIZIALIZZATO (socket inserito non risponde)
    }

    override fun requestInfo(req: ByteArray, len: Int) {
        var msg:ByteArray = req

        //Send the modbus request
        soc.outputStream.write(msg)

        //Put the modbus answer in out
        var out=ByteArray(len*2+5) //3B header + 2B crc

        soc.getInputStream().read(out)

        TODO("CRC check su out [ultimi 2 byte] + REQ TYPE check [primi 3 byte] + cleanup bytearray")

        packet = out
        notify("Packet updated")
    }

    override fun getState(): ByteArray?{
        return packet
        TODO()
    }

    fun calculateCRC(b: ByteArray): ByteArray{
        val crc = ModbusCRC()
        crc.update(b)
        val checkSum = crc.crcBytes
        println(String.format("CheckSum = 0x%02X, 0x%02X", checkSum[0], checkSum[1]))
        val new_b = b + checkSum
        return new_b
    }
}