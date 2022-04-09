package com.carbon7.virtualdisplay.ui.status

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.model.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class StatusViewModelTest {


    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var upsData : UpsData

    private lateinit var statusVM : StatusViewModel

    val exapleOfStatusMap = mapOf(
        "S000" to Status("S000", R.string.s000,false),
        "S001" to Status("S001", R.string.s001,true),
        "S002" to Status("S002", R.string.s002,false),
        "S003" to Status("S003", R.string.s003,true),
        "S004" to Status("S004", R.string.s004,false),
    )

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)

        statusVM = StatusViewModel()
        statusVM.load(upsData)

        statusVM.filteredStatus.observeForever {println(it)}
    }

    @Test
    fun filterAllTest(){
        Mockito.`when`(upsData.status).thenReturn(exapleOfStatusMap)

        statusVM.setCurrentFilter(StatusViewModel.Filter.ALL)
        statusVM.update()

        val codes = statusVM.filteredStatus.value!!.map { it.code }
        assertEquals(listOf("S000","S001","S002","S003","S004"), codes)
    }

    @Test
    fun filterActiveTest(){
        Mockito.`when`(upsData.status).thenReturn(exapleOfStatusMap)

        statusVM.setCurrentFilter(StatusViewModel.Filter.ACTIVE)
        statusVM.update()

        val codes = statusVM.filteredStatus.value!!.map { it.code }
        assertEquals(listOf("S001","S003"), codes)
    }

    @Test
    fun filterInactiveTest(){
        Mockito.`when`(upsData.status).thenReturn(exapleOfStatusMap)

        statusVM.setCurrentFilter(StatusViewModel.Filter.INACTIVE)
        statusVM.update()

        val codes = statusVM.filteredStatus.value!!.map { it.code }
        assertEquals(listOf("S000","S002","S004"),codes)
    }

    @Test
    fun voidTest(){
        Mockito.`when`(upsData.status).thenReturn(null)
        statusVM.update()

        val statusList = statusVM.filteredStatus.value

        assertNotNull(statusList)
        assertEquals(listOf<Status>(),statusList)
    }


}