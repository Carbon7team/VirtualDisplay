package com.carbon7.virtualdisplay.model

import androidx.annotation.StringRes

data class Measurement(val code:String, @StringRes val name:Int, var value:Float, @StringRes val unitMeasure:Int)
