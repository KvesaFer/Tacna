package com.mkves.tacna.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(data: String): List<Int> {
        if (data.isEmpty()) return emptyList()
        return data.split(",").map { it.toInt() }
    }
}