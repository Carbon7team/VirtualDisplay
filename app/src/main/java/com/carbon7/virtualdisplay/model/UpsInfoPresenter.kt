package com.carbon7.virtualdisplay.model

class UpsInfoPresenter: Observer {

    private val manager: ManagerInterface = DataManager()

    fun updateUI(){}

    fun updateModel(){}

    override fun update() {
        //Messi come placeholder, probabilmente ci va solo update UI
        updateUI()
        updateModel()
    }

}