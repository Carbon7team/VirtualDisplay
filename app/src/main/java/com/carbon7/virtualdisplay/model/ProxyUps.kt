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
        soc!!.close() //CRASHA SE soc NON E' INIZIALIZZATO (socket inserito non risponde)
    }

    override fun requestInfo() {
        val req = byteArrayOf(0x01,0x03,0x00,0x30,0x00,0x08) //Da cambiare l'ultimo byte quando si ha un XML completo
        val msg = calculateCrc(req)
        //Send the modbus request
        soc.outputStream.write(msg)

        //Put the modbus answer in out
        val size = (msg[4].toInt() + msg[5].toInt())*2 + 5
        val out = ByteArray(size)

        soc.getInputStream().read(out)

        val outPayload = out.copyOfRange(0,size-2)
        val outCRC = calculateCrc(outPayload)
        if (outCRC contentEquals out && out[0] == (0x01).toByte() && out[1] == (0x03).toByte()) {
            packet = out
            notify("Packet updated")
        }
        else{
            print("Response error - unexpected packet")
        }
        //("CRC check su out [ultimi 2 byte] + REQ TYPE check [primi 3 byte] + cleanup bytearray")
    }

    override fun getState(): ByteArray?{
        return packet
        TODO()
    }

    fun calculateCrc(b: ByteArray): ByteArray{
        val crc = ModbusCRC()
        crc.update(b)
        val checkSum = crc.crcBytes
        return b + checkSum
    }
}