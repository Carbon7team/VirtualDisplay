package com.carbon7.virtualdisplay

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.carbon7.virtualdisplay.model.SavedUpsDao
import com.carbon7.virtualdisplay.model.SavedUps
import com.carbon7.virtualdisplay.model.AppDB
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class AppDBTest {

    private lateinit var savedUpsDao: SavedUpsDao
    private lateinit var db: AppDB

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        savedUpsDao = db.dao
        savedUpsDao.addUps(SavedUps("test1", "127.0.0.1", 5001))
        savedUpsDao.addUps(SavedUps("test2", "127.0.0.2", 5002))
        savedUpsDao.addUps(SavedUps("test3", "127.0.0.3", 5003))
        savedUpsDao.addUps(SavedUps("test4", "127.0.0.4", 5004))
        savedUpsDao.addUps(SavedUps("test5", "127.0.0.5", 5005))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTwelve() {
        val u = SavedUps("test6", "127.0.0.6", 5006)
        savedUpsDao.addUps(u)
        val six = savedUpsDao.findById(6)
        assertEquals(six, u)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAndGet(){
        savedUpsDao.deleteUps(6)
        val list = savedUpsDao.getAll()
        assertEquals(list.count(), 5)
    }

    @Test
    @Throws(Exception::class)
    fun clearAndGetAll(){
        savedUpsDao.clearDB()
        val list = savedUpsDao.getAll()
        assertEquals(list, mutableListOf<SavedUps>())
    }
}