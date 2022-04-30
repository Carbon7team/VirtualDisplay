package com.carbon7.virtualdisplay.ui.status

import androidx.lifecycle.*
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.model.Observer
import com.carbon7.virtualdisplay.model.ProxyUps
import com.carbon7.virtualdisplay.model.Status
import com.carbon7.virtualdisplay.model.UpsData
import kotlinx.coroutines.launch

class StatusViewModel : ViewModel(), Observer {

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

    val filteredStatus : LiveData<List<Status>> = Transformations.switchMap(_status){ list ->
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