package com.carbon7.virtualdisplay.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class EventBus<T: Any>() {
    constructor(startValue: T) : this() {
        _events.postValue(startValue)
    }

    private val _events = MutableLiveData<T>()
    val events: LiveData<T> = _events

    suspend fun invokeEvent(event: T){
        //Log.d("MyApp", "NEW DATA PUBLISHED")
        _events.postValue(event)
    }
}


