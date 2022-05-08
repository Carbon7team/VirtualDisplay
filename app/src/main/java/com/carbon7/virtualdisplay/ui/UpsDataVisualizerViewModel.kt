package com.carbon7.virtualdisplay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.carbon7.virtualdisplay.model.Alarm
import com.carbon7.virtualdisplay.model.EventBus
import com.carbon7.virtualdisplay.model.Measurement
import com.carbon7.virtualdisplay.model.Status

abstract class UpsDataVisualizerViewModel: ViewModel() {


    private var currentSource : LiveData<Triple<List<Status>, List<Alarm>, List<Measurement>>>? = null

    /**
     * Change the event bus used as source
     *
     * @param bus the new eventBus
     */
    fun bind(bus: EventBus<Triple<List<Status>, List<Alarm>, List<Measurement>>>){
        //remove the old source (if exist)
        currentSource?.let {
            _status.removeSource(it)
            _alarms.removeSource(it)
            _measurements.removeSource(it)
        }
        //add the new source
        _status.addSource(bus.events){ _status.value=it.first!! }
        _alarms.addSource(bus.events){ _alarms.value=it.second!! }
        _measurements.addSource(bus.events){ _measurements.value=it.third!! }

        currentSource=bus.events
    }

    private val _status = MediatorLiveData<List<Status>>()
    private val _alarms = MediatorLiveData<List<Alarm>>()
    private val _measurements = MediatorLiveData<List<Measurement>>()

    protected open val status: LiveData<List<Status>> = _status
    protected open val alarms: LiveData<List<Alarm>> = _alarms
    protected open val measurement: LiveData<List<Measurement>> = _measurements

}