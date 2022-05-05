package com.carbon7.virtualdisplay.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class EventBus<T> {

    private val _events = MutableLiveData<T>()
    val events: LiveData<T> = _events

    suspend fun invokeEvent(event: T){
        //Log.d("MyApp", "NEW DATA PUBLISHED")
        _events.postValue(event!!)
    }
}


