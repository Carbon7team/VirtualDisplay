package com.carbon7.virtualdisplay.ui.ups_selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.carbon7.virtualdisplay.model.Observer
import com.carbon7.virtualdisplay.model.SavedUps
import com.carbon7.virtualdisplay.model.UpsData

class UpsSelectorViewModel : ViewModel(), Observer {
    lateinit var upsData : UpsData

    enum class Filter(val filterFun:(SavedUps)->Boolean){
        ALL({true})
    }

    fun load(upsData: UpsData){
        this.upsData = upsData
        upsData.addObserver(this)
        upsData.start()
    }

    private val _ups = MutableLiveData(
        listOf<SavedUps>()
    )

    private val _currentFilter : MutableLiveData<Filter> = MutableLiveData(Filter.ALL)

    val filteredStatus : LiveData<List<SavedUps>> = Transformations.switchMap(_ups){ list ->
        Transformations.map(_currentFilter){
            list?.filter(it.filterFun) ?: listOf()
        }
    }

    override fun update() {

    }
}
