package com.carbon7.virtualdisplay.model

import androidx.annotation.StringRes


data class Alarm(val code:String, @StringRes val name:Int, val level:Level, var isActive:Boolean = false){
    enum class Level{
        NONE,WARNING,CRITICAL
    }
}
