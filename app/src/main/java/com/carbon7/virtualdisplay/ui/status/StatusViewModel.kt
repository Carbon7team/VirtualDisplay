package com.carbon7.virtualdisplay.ui.status

import android.util.Log
import androidx.lifecycle.*
import com.carbon7.virtualdisplay.model.*
import com.carbon7.virtualdisplay.model.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusViewModel : ViewModel() {

    private lateinit var bus : EventBus<Triple<List<Status>,List<Alarm>,List<Measurement>>>

    init {
        Log.d("MyApp","statusVM created")


    }


    enum class Filter(val filterFun:(Status)->Boolean){
        ALL({true}),
        ACTIVE({it.isActive}),
        INACTIVE({!it.isActive})
    }

    lateinit var job: Job
    fun load(bus: EventBus<Triple<List<Status>,List<Alarm>,List<Measurement>>>){
        Log.d("MyApp","statusVM LOADED")


        job = viewModelScope.launch {
            bus.events.collect{
                Log.d("MyApp", "NEW DATA COLLECTED")
                _status.value= it.first
            }
        }
    }
    fun unload(){
        job.cancel()
    }



    private val _status = MutableLiveData(listOf<Status>())

    private val _currentFilter : MutableLiveData<Filter> = MutableLiveData(Filter.ALL)
    val currentFilter : LiveData<Filter> = _currentFilter

    val filteredStatus : LiveData<List<Status>> = Transformations.switchMap(_status){ list ->
        Transformations.map(_currentFilter){
            list?.filter(it.filterFun) ?: listOf()
        }
    }



    fun setCurrentFilter(f:Filter){
        _currentFilter.postValue(f)

    }

    /*override fun update() {
        _status.postValue(
            upsData.status
        )
    }*/


    override fun onCleared() {
        Log.d("MyApp","statusVM cleared")
    }

}