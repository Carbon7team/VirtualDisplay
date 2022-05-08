package com.carbon7.virtualdisplay.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * An event bus to pass event (and data) to subscribers
 *
 * @param T type of the event (only not null types)
 */
class EventBus<T: Any>() {
    /**
     * @constructor
     * Create the EventBus and set a starting event
     *
     * @param startingEvent the starting event
     */
    constructor(startingEvent: T) : this() {
        _events.postValue(startingEvent)
    }


    private val _events = MutableLiveData<T>()
    val events: LiveData<T> = _events

    /**
     * Invoke a new event (from the publisher)
     *
     * @param event event to pass to subscribers
     */
    fun invokeEvent(event: T){
        //Log.d("MyApp", "NEW DATA PUBLISHED")
        _events.postValue(event)
    }
}


