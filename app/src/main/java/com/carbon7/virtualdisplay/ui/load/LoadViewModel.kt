package com.carbon7.virtualdisplay.ui.load

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.carbon7.virtualdisplay.model.Measurement
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerViewModel

class LoadViewModel : UpsDataVisualizerViewModel() {

    enum class Filter(val filterFun:(Measurement)->Boolean){
        ALL({true})
    }

    private val _currentFilter : MutableLiveData<Filter> = MutableLiveData(Filter.ALL)

    val filteredMeasurements = Transformations.switchMap(measurement){ list ->
        Transformations.map(_currentFilter){
            list.filter(it.filterFun)
        }
    }
}