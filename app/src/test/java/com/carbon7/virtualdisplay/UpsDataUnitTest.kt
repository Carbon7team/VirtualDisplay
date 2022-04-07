package com.carbon7.virtualdisplay

import com.carbon7.virtualdisplay.model.Observer
import com.carbon7.virtualdisplay.model.Ups
import com.carbon7.virtualdisplay.model.UpsData

import org.junit.Assert.*
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito


class UpsDataUnitTest{

    @Test
    fun prova(){
        assertEquals(true,true)
    }

    @Nested
    inner class DecodeTest{
        lateinit var upsData : UpsData
        lateinit var obs : Observer

        @BeforeEach
        fun before(){
            println("Before")
            upsData= UpsData(FakeUps())
        }
        @AfterEach
        fun after(){
            println("After")
            upsData.addObserver(obs)

            upsData.start()
            Thread.sleep(50)//wait that upsData thread start
            upsData.stop()

            Mockito.verify(obs).update()
        }

        @Test
        fun testNotNull(){
            obs = Mockito.mock(Observer::class.java)
            Mockito.`when`(obs.update()).then {
                println("T1")
                assertNotNull(upsData.status)
                assertNotNull(upsData.alarms)
            }
        }

        @Test
        fun testDataCount(){
            obs = Mockito.mock(Observer::class.java)
            Mockito.`when`(obs.update()).then {
                println("T2")
                assertEquals(128, upsData.status!!.size)
                assertEquals(6, upsData.alarms!!.size)
                assertEquals(3, upsData.status!!.filter { it.value.isActive }.size)
            }
        }

        @Test
        fun testDataCode(){
            obs = Mockito.mock(Observer::class.java)
            Mockito.`when`(obs.update()).then {
                println("T3")
                upsData.status!!.forEach { (k, v) -> assertEquals(k, v.code) }
                upsData.alarms!!.forEach { (k, v) -> assertEquals(k, v.code) }
            }
        }
    }


}