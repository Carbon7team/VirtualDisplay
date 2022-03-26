package com.carbon7.virtualdisplay.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.Inet4Address

@Entity
data class SavedUps (
    @PrimaryKey
    val ID: Int,
    val name: String,
    val address: String,
    val port: Int
)