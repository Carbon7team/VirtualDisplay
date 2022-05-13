package com.carbon7.virtualdisplay.ui.ups_selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carbon7.virtualdisplay.model.SavedUpsDao

class UpsSelectorViewModelFactory(private val dao: SavedUpsDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpsSelectorViewModel::class.java)) {
            return UpsSelectorViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}