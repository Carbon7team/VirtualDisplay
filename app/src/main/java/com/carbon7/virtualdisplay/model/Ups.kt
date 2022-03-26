package com.carbon7.virtualdisplay.model

abstract class Ups: Subject() {
    abstract fun requestInfo()
    abstract fun getState(): String
    abstract fun close()
}