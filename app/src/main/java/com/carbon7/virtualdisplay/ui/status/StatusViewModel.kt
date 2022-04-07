package com.carbon7.virtualdisplay.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.model.Observer
import com.carbon7.virtualdisplay.model.ProxyUps
import com.carbon7.virtualdisplay.model.Status
import com.carbon7.virtualdisplay.model.UpsData

class StatusViewModel : ViewModel(), Observer {

    private val upsData = UpsData(ProxyUps("192.168.11.178",8888))
    init {
        upsData.addObserver(this)
        upsData.start()
    }

    private val _status = MutableLiveData(
        listOf<Status>()
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

    override fun update() {
        _status.postValue(
            upsData.status?.values?.toList()
        )
    }


}