package com.carbon7.virtualdisplay.ui.status

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.model.Status

class StatusViewModel : ViewModel() {

    private val _status = MutableLiveData(
        listOf(
            Status("S000", R.string.S000,true),
            Status("S001",R.string.S001,true),
            Status("S002",R.string.S002,false)
        )
    )
    var status : LiveData<List<Status>> = _status


    fun addS000(b:Boolean){
        val l =_status.value!!.toMutableList()
        l.add(Status("S000", R.string.S000,b))
        _status.value= l
    }
    fun addS001(b:Boolean){
        val l =_status.value!!.toMutableList()
        l.add(Status("S001", R.string.S001,b))
        _status.value= l
    }
    fun addS002(b:Boolean){
        val l =_status.value!!.toMutableList()
        l.add(Status("S002", R.string.S002,b))
        _status.value= l
    }


// TODO: Implement the ViewModel
}