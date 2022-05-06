package com.carbon7.virtualdisplay.model

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.*


/**
 *
 *
 * @constructor
 * Create a ProxyUPS realted to the Ups at the given socket
 *
 * @param ip address of the UPS (ipv4)
 * @param port port of the UPS where modbus server is opened
 */
class ProxyUps(ip: String, port: Int): Ups() {
    private var soc : Socket? = null
    private val addr = InetSocketAddress(ip,port)


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

    /**
     * Establish a connection with the UPS
     */
    override fun open(){
        if(soc!=null && soc!!.isConnected)
            reconnect()
    }

    /**
     * Close the connection with the UPS
     */
    override fun close(){
        soc?.close() //CRASHA SE soc NON E' INIZIALIZZATO (socket inserito non risponde)
    }

    /**
     * Establish a connection with the UPS,
     * if there is already another connection it will be interrupted
     */
    private fun reconnect(){
        CoroutineScope(Dispatchers.IO).launch{
            soc?.close()
            try {
                //soc = Socket(InetAddress.getByName(_ip),_port)
                soc = Socket()
                soc!!.connect(addr)

            }catch (e: SocketException){
                //Log.d("myApp", e.toString())
            }catch (e:ConnectException){
                Log.d("myApp", e.toString())
            }
        }
    }

    /**
     * It make a request to the UPS and return the response
     *
     * @return The Result of the request:
     *              -ByteArray if it success;
     *              -an Exception if it fail
     */
    override suspend fun requestInfo(): Result<ByteArray> {
        if(soc==null) {
            reconnect()
            return Result.failure(Exception("Socket non inizializzato"))
        }
        return withContext(Dispatchers.IO) {
            try {
                val req = byteArrayOf(0x01, 0x03, 0x00, 0x30, 0x00, 0x60) //Da cambiare l'ultimo byte quando si ha un XML completo
                val msg = req + calculateCrc(req)
                //Send the modbus request
                soc!!.outputStream.write(msg)

                //Put the modbus answer in out
                val size = (msg[4].toInt() + msg[5].toInt()) * 2 + 5
                val out = ByteArray(size)

                soc!!.getInputStream().read(out)
                if (checkCrc(out.copyOfRange(0,size-2),out.copyOfRange(size-2,size))
                    && out[0] == (0x01).toByte()
                    && out[1] == (0x03).toByte())

                    return@withContext  Result.success(out.copyOfRange(3,size-2))

                throw Exception("Errore nella messaggio(raw) ricevuto")
            }catch (e: SocketException){
                reconnect()
                return@withContext Result.failure(e)
            }catch (e: Exception){
                return@withContext Result.failure(e)
            }            //("CRC check su out [ultimi 2 byte] + REQ TYPE check [primi 3 byte] + cleanup bytearray")

        }
    }

    /**
     * Control if the checksum is correct
     *
     * @param bytes bytes of data
     * @param checksum crc of the data bytes
     * @return if the checksum is correct
     */
    private fun checkCrc(bytes: ByteArray, checksum:ByteArray):Boolean{
        val crc = ModbusCRC()
        crc.update(bytes)
        return crc.crcBytes.contentEquals(checksum)
    }

    /**
     * Calculate the checksum for
     *
     * @param bytes bytes of data
     * @return crc of the data bytes
     */
    private fun calculateCrc(bytes: ByteArray): ByteArray{
        val crc = ModbusCRC()
        crc.update(bytes)
        return crc.crcBytes
    }
}