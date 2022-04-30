package com.carbon7.virtualdisplay.model

abstract class Ups{
    abstract suspend fun requestInfo(): ByteArray
    abstract fun close()
}