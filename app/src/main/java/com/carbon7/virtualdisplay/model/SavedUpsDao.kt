package com.carbon7.virtualdisplay.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedUpsDao {
    @Insert
    fun addUps(ups: SavedUps)

    @Query("SELECT * from saved_ups WHERE ID = :key")
    fun findById(key: Int): SavedUps

    @Query("SELECT * from saved_ups")
    fun getAll(): Flow<List<SavedUps>>

    @Query("DELETE from saved_ups WHERE ID = :key")
    fun deleteUps(key: Int)

    @Update
    fun updateUps(ups: SavedUps)

    @Query("DELETE from saved_ups")
    fun clearDB()
}