package com.carbon7.virtualdisplay.model

import android.util.Log
import org.json.JSONObject
import java.net.*
import kotlin.concurrent.thread

/**
 * Try to connect to the modbus using UDP over TCP protocols
 *
 * @param ip Ipv4 address of the modbus
 * @param port Port of the modbus
 * @param onConnection what to do when the connection is established
 * @param onClose what to do when the connection is closed normally or by an error
 * @constructor
 * @throws IllegalArgumentException the ip and/or port used are not valid
 */
class ModbusConnection (ip:String, port:Int, onConnection:(ModbusConnection)->Unit, private val onClose:(err:String?)->Unit){
    private lateinit var soc: Socket
    //private var mbThread : Thread? = null

    companion object{
        private val ipv4_pattern=
            ("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$").toRegex()
    }

    //private enum class Requests(val hex:String,val len:Int){
    //    STATUS("0103003000084403",8),
    //    ALARMS("010300380008C5C1",8),
    //   MEASUREMENTS("0103004000504422",80)
    //}

    init {
        if(!ipv4_pattern.matches(ip))
            throw IllegalArgumentException("Indirizzo ip non valido")
        if(!(port in 0..35565))//port not in 0..35565
            throw IllegalArgumentException("Porta non valida")
        soc = Socket()
        thread {
            try {
                soc.connect(InetSocketAddress(ip,port),5000)
                onConnection(this)
            }catch (e:SocketTimeoutException){
                Log.d("myApp", e.toString())
                onClose("Impossibile connettersi all'host")
            }
        }
    }

    private fun hexString2ByteArray(hex:String):ByteArray{
        return hex.chunked(2).map{it.toInt(16).toByte()}.toByteArray()
    }

    fun sendData(req: String):ByteArray{
        //Converts hex string in bytes
        var msg:ByteArray = hexString2ByteArray(req)

        //Send the modbus request
        soc.outputStream.write(msg)

        //Put the modbus answer in out
        var out=ByteArray(req.length*2+5) //3B header + 2B crc
        soc.getInputStream().read(out)

        //Return only the bytes of the payload
        return out.copyOfRange(3,out.size-2)
    }

    /**
     * Request data every N seconds and execute the lambda
     *
     * @param millis how often requests data to the modbus
     * @param onUpdateData what to do when modbus send data
     */

    /**
     * Close the connection with the modbus
     *
     */
    fun close(){
        soc.close() //CRASHA SE soc NON E' INIZIALIZZATO (socket inserito non risponde)
    }

    /**
     * Make the request to the modbus
     *
     * @param req Type of request to do to the modbus
     * @return The payload of the answer
     */
    /*
    private fun makeModBusReq(req: Requests):ByteArray{
        //Converts hex string in bytes
        var msg:ByteArray=hexString2ByteArray(req.hex)

        //Send the modbus request
        soc.outputStream.write(msg)

        //Put the modbus answer in out
        var out=ByteArray(req.len*2+5) //3B header + 2B crc
        soc.getInputStream().read(out)

        //Return only the bytes of the payload
        return out.copyOfRange(3,out.size-2)
    }
    */
}