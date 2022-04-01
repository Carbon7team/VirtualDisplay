package com.carbon7.virtualdisplay.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carbon7.virtualdisplay.model.Status

class StatusViewModel : ViewModel() {

    private var status : LiveData<List<Status>> = MutableLiveData(listOf(
        Status("A001","Prova",true),
        Status("A002","Bla Bla",true),
        Status("A003","lalalalalal",false)
    ))



// TODO: Implement the ViewModel
}