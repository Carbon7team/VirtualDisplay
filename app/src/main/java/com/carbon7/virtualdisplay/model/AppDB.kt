package com.carbon7.virtualdisplay.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedUps::class], version = 1, exportSchema = false)
abstract class AppDB: RoomDatabase() {
    abstract val dao: SavedUpsDao
    companion object{
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getInstance(con: Context): AppDB {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        con.applicationContext,
                        AppDB::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}