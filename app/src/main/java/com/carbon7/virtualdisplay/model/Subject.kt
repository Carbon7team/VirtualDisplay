package com.carbon7.virtualdisplay.model


abstract class Subject {

    var observerList: MutableList<Observer> = mutableListOf()

    fun addObserver(obs: Observer){
        if (!observerList.contains(obs)){
            observerList.add(obs)
        }
    }


    fun removeObserver(obs: Observer){
        observerList.remove(obs)
    }

    protected fun notify(event: String? = null){
        for(i in 0 until observerList.size)
            observerList[i].update()
        /*for (o in observerList) {
            o.update()
        }*/
    }
}