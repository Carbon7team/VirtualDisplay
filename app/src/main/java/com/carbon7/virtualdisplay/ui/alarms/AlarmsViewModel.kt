package com.carbon7.virtualdisplay.ui.alarms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.carbon7.virtualdisplay.model.Alarm
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerViewModel

class AlarmsViewModel : UpsDataVisualizerViewModel() {

    enum class Filter(val filterFun:(Alarm)->Boolean){
        ALL({true}),
        WARNING({it.level== Alarm.Level.WARNING}),
        CRITICAL({it.level== Alarm.Level.CRITICAL})
    }

    private val _currentFilter : MutableLiveData<Filter> = MutableLiveData(Filter.ALL)
    val currentFilter : LiveData<Filter> = _currentFilter

    val filteredAlarms = Transformations.switchMap(alarms){ list ->
        Transformations.map(_currentFilter){
            list.filter(Alarm::isActive).filter(it.filterFun)
        }
    }

    fun setCurrentFilter(f: Filter){
        _currentFilter.postValue(f)
    }

}