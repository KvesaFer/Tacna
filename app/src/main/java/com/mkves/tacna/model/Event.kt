package com.mkves.tacna.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "events", indices = [Index(value = ["eventName"], unique = true)])
data class Event(
    var eventName: String,
    var beerPrice: Double,
    var juicePrice: Double,
    var shotPrice: Double,
    var payoutPercentage: Double,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var numBeers: Int = 0,
    var numJuices: Int = 0,
    var numShots: Int = 0,

    var beerHistory: List<Int> = emptyList(),
    var juiceHistory: List<Int> = emptyList(),
    var shotsHistory: List<Int> = emptyList()

) {
    val sales: Double
        get() = beerPrice * numBeers + juicePrice * numJuices + shotPrice * numShots
    val payout: Double
        get() = sales * payoutPercentage
}