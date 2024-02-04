package com.mkves.tacna.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["eventId"], unique = true)],
    foreignKeys = [ForeignKey(entity = Event::class,
        parentColumns = ["id"],
        childColumns = ["eventId"],
        onDelete = ForeignKey.CASCADE)])
data class UserFunds(
    var eventId: Int,
    var myDeposit: Double = 0.0,
    var givenDeposit: Double = 0.0,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    val totalFunds: Double
        get() = myDeposit + givenDeposit
}