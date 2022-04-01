package com.carbon7.virtualdisplay

import com.carbon7.virtualdisplay.model.ProxyUps
import org.junit.Test
import org.junit.Assert.*


class ProxyUPSUnitTest {

    private fun String.toPositiveInt(i: Int) = toInt(i) and 0xFF

    private fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Must have an even length" }
        return chunked(2)
            .map { it.toPositiveInt(16).toByte() }
            .toByteArray()
    }

    @Test
    fun calculateCrcTest(){
        val b: ByteArray = byteArrayOf(0x01,0x03,0x00,0x30,0x00,0x08)
        val p = ProxyUps("127.0.0.1", 8888)
        val test = p.calculateCrc(b)
        val actual = b + byteArrayOf(0x44,0x03)
        assertArrayEquals(test, actual)
    }

    @Test
    fun getStateTest(){
        val p = ProxyUps("127.0.0.1", 8888)
        val res = p.getState()
        assertEquals(res,null)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun requestAndGetInfoTest() {
        val p = ProxyUps("192.168.1.13", 8888)
        p.requestInfo()
        //val actual = byteArrayOf(0x01,0x03,0x10,0x55,0xAC.toByte(),0x55,0xAC.toByte(),55,0xAC.toByte(),55,0xAC.toByte(),55,0xAC.toByte(),55,0xAC.toByte(),55,0xAC.toByte(),55,0xAC.toByte(),0xAC.toByte(),0x6F)
        val s = "01031055ac55ac55ac55ac55ac55ac55ac55ace76f"
        val actual = s.decodeHex()
        print(actual.contentToString())
        assertArrayEquals(p.getState(),actual)
    }
}