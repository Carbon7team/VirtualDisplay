package com.carbon7.virtualdisplay.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedUpsDao {
    @Insert
    fun addUps(ups: SavedUps)

    @Query("SELECT * from saved_ups WHERE ID = :key")
    fun findById(key: Int): SavedUps

    @Query("SELECT * from saved_ups")
    fun getAll(): MutableList<SavedUps>

    @Query("DELETE from saved_ups WHERE ID = :key")
    fun deleteUps(key: Int)

    @Query("DELETE from saved_ups")
    fun clearDB()
}