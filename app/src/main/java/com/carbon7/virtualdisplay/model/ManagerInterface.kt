package com.carbon7.virtualdisplay.model

interface ManagerInterface {
    fun setActiveUPS(id: Int)

    fun exitFromUps()

    fun getStatus(): Map<String, String>

    fun getAlarms(): Map<String, String>

    fun getMeasurements(): Map<String, String>

    fun getSavedUpses(): List<SavedUps>

    fun addSavedUps(ups: SavedUps)

    fun removeSavedUps(id: Int)

    fun login(username:String, password:String)
}