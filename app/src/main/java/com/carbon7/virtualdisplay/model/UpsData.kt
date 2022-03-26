package com.carbon7.virtualdisplay.model

import android.location.GnssMeasurement

class UpsData(u: Ups? = null): Observer{
    private var ups = u
    internal lateinit var status: Map<String, String>
    internal lateinit var alarms: Map<String, String>
    internal lateinit var measurements: Map<String, String>


    override fun update() {
        val packet = ups!!.getState()
        TODO("elaborate packet and set the attributes accordingly")
    }

    fun setUps(u: Ups?){
        ups!!.removeObserver(this)
        //ups.close()
        ups = u
        ups!!.addObserver(this)
    }

    /* private fun decodeStatus(statusBytes: ByteArray):List<Boolean>{
        return statusBytes.toBinaryString().map{it=='1'}
    }
    private fun decodeAllarms(allarmBytes: ByteArray): List<Boolean> {
        return allarmBytes.toBinaryString().map { it == '1' }
    }
    private fun decodeMesaurements(measurementBytes: ByteArray): List<Any> {
        val measurements_raw= measurementBytes.take(77*2).toByteArray().toShorts()//Da M000 a M076

        var measurements:MutableList<Any> = measurements_raw.zip(measurement_factor){
                value,factor->
            var measurement= value
            if(factor[2]==true)
                measurement= (value-32768).toShort()

            if(factor[reg0x00E]==1)
                return@zip measurement
            return@zip measurement.toFloat()/(factor[reg0x00E] as Int)
        }.toMutableList()

        measurements.add("BOH")//M077 to understand
        measurements.add("0x"+measurementBytes.copyOfRange(78*2,79*2).toHexString())//M078 status led color
        measurements.add(measurementBytes.copyOfRange(79*2,80*2).toShorts()[0]) //M079 ACK alarm number

        //var measurements_log=measurements.mapIndexed{idx,value -> "M%03d:${value}".format(idx)}
        //Log.d("ModBus",measurements_log.toString())

        return measurements.toList()
    }

    private fun hexString2ByteArray(hex:String):ByteArray{
        return hex.chunked(2).map{it.toInt(16).toByte()}.toByteArray()
    }
    private fun ByteArray.toHexString(separator:String=""):String{
        return this.map{"%02x".format(it)}.joinToString(separator)
    }
    private fun ByteArray.toBinaryString(separator:String=""):String{
        return this.map{"%02x".format(it).toInt(16).toBinary(8)}.joinToString(separator)
    }
    private fun ByteArray.toShorts():List<Short>{
        return this.toList().chunked(2).map{(it[0].toShort()*256+it[1].toShort()).toShort()}
    }
    private fun Int.toBinary(len: Int): String {
        return String.format("%" + len + "s", this.toString(2)).replace(" ".toRegex(), "0")
    }
    */

}