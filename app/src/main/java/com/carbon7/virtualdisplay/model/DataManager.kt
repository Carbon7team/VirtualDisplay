package com.carbon7.virtualdisplay.model

class DataManager: Subject(), ManagerInterface {

    private val database: MutableList<SavedUps> = mutableListOf()
    private var currentUps: SavedUps? = null
    private var data: UpsData = UpsData()

    override fun setActiveUPS(id: Int){
        TODO()
        /*currentUps = getUps(id)
        addr =
        p =
        ups = ProxyUps(addr, p)
        data.setUps(ups)*/
    }

    override fun exitFromUps(){
        data.setUps(null)
        TODO()
    }

    override fun getStatus(): Map<String, String> {
        return data.status
    }

    override fun getAlarms(): Map<String, String> {
        return data.alarms
    }

    override fun getMeasurements(): Map<String, String> {
        return data.measurements
    }

    override fun getSavedUpses(): List<SavedUps>{
        return database
    }

    override fun addSavedUps(ups: SavedUps){
        database.add(ups)
    }

    override fun removeSavedUps(id: Int){ //Modificherei il parametro con uno di tipo SavedUps
        TODO()
    }

    override fun login(username:String, password:String){
        TODO()
    }
}