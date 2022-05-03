package com.carbon7.virtualdisplay.ui.ups_selector

import androidx.lifecycle.*
import com.carbon7.virtualdisplay.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UpsSelectorViewModel : ViewModel() {
    lateinit var dao : SavedUpsDao

    fun load(d: SavedUpsDao){
        dao = d

        CoroutineScope(Dispatchers.Default).launch {
            dao.getAll().collect {
                _ups.postValue(it)
            }
        }
    }

    private val _ups = MutableLiveData(
        listOf<SavedUps>()
    )

    val ups:LiveData<List<SavedUps>> = _ups


    fun addUps(name: String, ip: String, port: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addUps(SavedUps(name, ip, port))
        }
    }

    fun modifyUps(id: Int, name: String?, ip: String?, port: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUps = dao.findById(id)

            if (name != null) currentUps.name = name
            if (ip != null) currentUps.address = ip
            if (port != null) currentUps.port = port

            dao.updateUps(currentUps)
        }
    }

    fun deleteUps(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteUps(id)
        }
    }
}
