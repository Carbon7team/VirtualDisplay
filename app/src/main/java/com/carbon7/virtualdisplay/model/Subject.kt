package com.carbon7.virtualdisplay.model


abstract class Subject {

    var observerList: MutableList<Observer> = mutableListOf()

    fun addObserver(obs: Observer){
        if (!observerList.contains(obs)){
            observerList.add(obs)
        }
    }

    fun removeObserver(obs: Observer){ observerList.remove(obs)}

    fun notify(event: String?){
        for (o in observerList){o.update()}
    }
}