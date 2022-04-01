package com.carbon7.virtualdisplay.model

abstract class UpsInfoPresenter: Observer {

    private val manager: DataManager = DataManager()

    fun updateUI(){}

    fun updateModel(){}

    override fun update() {
        updateUI()
        updateModel()
    }

}