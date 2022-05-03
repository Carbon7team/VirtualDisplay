package com.carbon7.virtualdisplay.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class ProxyUps(ip: String, port: Int): Ups() {
    private var soc = Socket()
    private val _ip = ip
    private val _port = port

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

    }

    override fun open(){
        thread {
            try {
                soc.connect(InetSocketAddress(_ip,_port),5000)
            }catch (e:SocketTimeoutException){
                Log.d("myApp", e.toString())
            }
        }
    }
    override fun close(){
        soc.close() //CRASHA SE soc NON E' INIZIALIZZATO (socket inserito non risponde)
    }

    override suspend fun requestInfo(): ByteArray {
        return withContext(Dispatchers.IO) {
            //Log.d("MyApp", "DDDD")
            val req = byteArrayOf(0x01, 0x03, 0x00, 0x30, 0x00, 0x60) //Da cambiare l'ultimo byte quando si ha un XML completo
            val msg = req + calculateCrc(req)
            //Send the modbus request
            soc.outputStream.write(msg)

            //Put the modbus answer in out
            val size = (msg[4].toInt() + msg[5].toInt()) * 2 + 5
            val out = ByteArray(size)

            soc.getInputStream().read(out)


            if (checkCrc(out.copyOfRange(0,size-2),out.copyOfRange(size-2,size))
                    && out[0] == (0x01).toByte()
                    && out[1] == (0x03).toByte())

                return@withContext out.copyOfRange(3,size-2)

            throw Exception("Errore nella messaggio(raw) ricevuto")

            //("CRC check su out [ultimi 2 byte] + REQ TYPE check [primi 3 byte] + cleanup bytearray")

        }
    }

    private fun checkCrc(bytes: ByteArray, checksum:ByteArray):Boolean{
        val crc = ModbusCRC()
        crc.update(bytes)
        return crc.crcBytes.contentEquals(checksum)
    }

    private fun calculateCrc(b: ByteArray): ByteArray{
        val crc = ModbusCRC()
        crc.update(b)
        return crc.crcBytes
    }
}