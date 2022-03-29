package com.carbon7.virtualdisplay.model

abstract class Ups: Subject() {
    abstract fun requestInfo(req: ByteArray, len: Int)
    abstract fun getState(): ByteArray?
    abstract fun close()
}