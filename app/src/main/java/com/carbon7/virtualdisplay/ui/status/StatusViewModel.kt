package com.carbon7.virtualdisplay.ui.status

import android.util.Log
import androidx.lifecycle.*
import com.carbon7.virtualdisplay.model.*
import com.carbon7.virtualdisplay.model.Observer
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusViewModel : UpsDataVisualizerViewModel() {

    enum class Filter(val filterFun:(Status)->Boolean){
        ALL({true}),
        ACTIVE({it.isActive}),
        INACTIVE({!it.isActive})
    }

    private val _currentFilter : MutableLiveData<Filter> = MutableLiveData(Filter.ALL)
    val currentFilter : LiveData<Filter> = _currentFilter

    val filteredStatus = Transformations.switchMap(_status){ list ->
        Transformations.map(_currentFilter){
            list.filter(it.filterFun)
        }
    }


    fun setCurrentFilter(f:Filter){
        _currentFilter.postValue(f)
    }
}