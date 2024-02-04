package com.mkves.tacna.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import com.mkves.tacna.model.Event
import com.mkves.tacna.model.UserFunds

@Dao
interface EventDao {
    @Insert
    fun insert(newEvent: Event): Long

    @Query("SELECT * FROM events")
    fun getAllEvents(): List<Event>

    @Update
    fun updateEvent(event: Event)

    @Query("SELECT * FROM events WHERE id =:arg0")
    fun getEventById(id: Int): Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateEvent(event: Event): Long

    @Query("SELECT * FROM events WHERE eventName = :arg0")
    fun getEventByName(eventName: String): Event?

}