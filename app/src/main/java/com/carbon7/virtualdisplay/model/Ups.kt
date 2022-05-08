package com.carbon7.virtualdisplay.model

abstract class Ups{
    abstract suspend fun requestInfo(): Result<ByteArray>
    abstract fun close()
    abstract fun open()
}