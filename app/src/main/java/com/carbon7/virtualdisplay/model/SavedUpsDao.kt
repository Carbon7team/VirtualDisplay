package com.carbon7.virtualdisplay.model

import androidx.room.Dao

@Dao
interface SavedUpsDao {
    fun getAll()
    fun findByName()
    fun addUps(ups: SavedUps)
    fun deleteUpd(upd: SavedUps)
}