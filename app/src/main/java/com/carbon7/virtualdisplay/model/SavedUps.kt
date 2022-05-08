package com.carbon7.virtualdisplay.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.Inet4Address

@Entity(tableName = "saved_ups")
data class SavedUps (

    @ColumnInfo(name = "ups_name")
    var name: String,

    @ColumnInfo(name = "ups_ip")
    var address: String,

    @ColumnInfo(name = "ups_port")
    var port: Int)
{
    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0}