package com.carbon7.virtualdisplay.ui.ups_selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.carbon7.virtualdisplay.model.Observer
import com.carbon7.virtualdisplay.model.Status
import com.carbon7.virtualdisplay.model.Ups
import com.carbon7.virtualdisplay.model.UpsData

class UpsSelectorViewModel : ViewModel(), Observer {
    lateinit var upsData : UpsData

    enum class Filter(val filterFun:(Status)->Boolean){
        ALL({true}),
        ACTIVE({it.isActive}),
        INACTIVE({!it.isActive})
    }

    fun load(upsData: UpsData){
        this.upsData = upsData
        upsData.addObserver(this)
        upsData.start()
    }

    private val _status = MutableLiveData(
        listOf<Status>()
    )

    private val _currentFilter : MutableLiveData<Filter> = MutableLiveData(Filter.ALL)
    val currentFilter : LiveData<Filter> = _currentFilter

    val filteredStatus : LiveData<List<Ups>> = Transformations.switchMap(_status){ list ->
        Transformations.map(_currentFilter){
            list?.filter(it.filterFun) ?: listOf()
        }
    }

    fun setCurrentFilter(f:Filter){
        _currentFilter.postValue(f)
    }

    override fun update() {
        _status.postValue(
            upsData.status?.values?.toList()
        )
    }
}