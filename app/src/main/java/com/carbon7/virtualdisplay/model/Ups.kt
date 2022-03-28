package com.carbon7.virtualdisplay.model

abstract class Ups: Subject() {
    abstract fun requestInfo(s: String)
    abstract fun getState(): ByteArray?
    abstract fun close()
}