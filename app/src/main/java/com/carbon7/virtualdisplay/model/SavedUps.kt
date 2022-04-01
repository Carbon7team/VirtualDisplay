package com.carbon7.virtualdisplay.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.Inet4Address

@Entity(tableName = "saved_ups")
data class SavedUps (

    @ColumnInfo(name = "ups_name")
    val name: String,

    @ColumnInfo(name = "ups_ip")
    val address: String,

    @ColumnInfo(name = "ups_port")
    val port: Int)
{
    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0}