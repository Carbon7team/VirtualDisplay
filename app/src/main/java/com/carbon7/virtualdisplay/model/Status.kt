package com.carbon7.virtualdisplay.model

import androidx.annotation.StringRes

data class Status(val code:String, @StringRes val name:Int, var isActive:Boolean = false)
