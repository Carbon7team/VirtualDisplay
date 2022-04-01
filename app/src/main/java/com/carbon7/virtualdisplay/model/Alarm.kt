package com.carbon7.virtualdisplay.model


data class Alarm(val code:String, val name:String, val level:Level, var isActive:Boolean = false){
    enum class Level{
        NONE,WARNING,CRITICAL
    }
}
