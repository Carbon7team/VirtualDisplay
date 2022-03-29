package com.carbon7.virtualdisplay

import com.carbon7.virtualdisplay.model.ProxyUps
import org.junit.Test
import org.junit.Assert.*


class ProxyUPSUnitTest {
    @Test
    fun CRC_correctness(){
        var b: ByteArray = byteArrayOf(1,3,0,48,0,8)
        val p = ProxyUps("192.168.1.13", 8888)
        val crc_res = p.calculateCRC(b)
        val crc_ref = ByteArray(8)
        for (i in 0..5){
            crc_ref[i]=b[i]
        }
        crc_ref[6]=0x44
        crc_ref[7]=0x03
        print(String.format("calc = 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X \n", crc_res[0], crc_res[1], crc_res[2], crc_res[3], crc_res[4], crc_res[5], crc_res[6], crc_res[7]))
        print(String.format("ref = 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X, 0x%02X \n", crc_ref[0], crc_ref[1], crc_ref[2], crc_ref[3], crc_ref[4], crc_ref[5], crc_ref[6], crc_ref[7]))
        assertArrayEquals(crc_res, crc_ref)
    }

}