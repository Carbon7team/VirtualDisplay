package com.carbon7.virtualdisplay.model

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class EventBus<T> {

    private val _events = MutableSharedFlow<T>()
    val events = _events.asSharedFlow()

    suspend fun invokeEvent(event: T) = _events.emit(event)
}


