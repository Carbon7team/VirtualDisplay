package com.carbon7.virtualdisplay.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.model.Status

class StatusViewModel : ViewModel() {

    private val _status = MutableLiveData(
        mutableListOf(
            Status("S000", R.string.S000,true),
            Status("S001",R.string.S001,true),
            Status("S002",R.string.S002,false)
        )
    )

    private val statusFilter : MutableLiveData<(Status)->Boolean> = MutableLiveData({true})

    var currentStatus : LiveData<List<Status>> = Transformations.switchMap(_status){list ->
        Transformations.map(statusFilter){
            list.filter(it)
        }
    }

    fun filterActiveStatus(){
        statusFilter.value = {it.isActive}
    }
    fun filterInctiveStatus(){
        statusFilter.value = {!it.isActive}
    }
    fun filterAllStatus(){
        statusFilter.value = {true}
    }

    fun addS000(b:Boolean){
        _status.value?.add(Status("S000", R.string.S000,b))
        _status.value=_status.value
    }
    fun addS001(b:Boolean){
        _status.value?.add(Status("S001", R.string.S001,b))
        _status.value=_status.value
    }
    fun addS002(b:Boolean){
        _status.value?.add(Status("S002", R.string.S002,b))
        _status.value=_status.value
    }


// TODO: Implement the ViewModel
}