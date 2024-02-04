package com.mkves.tacna.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mkves.tacna.model.UserFunds

@Dao
interface UserFundsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(userFunds: UserFunds): Long

    @Query("SELECT * FROM UserFunds WHERE eventId = :arg0")
    fun getUserFundsByEventId(eventId: Int): UserFunds
}