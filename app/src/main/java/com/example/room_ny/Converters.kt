package com.example.room_ny

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {

    @TypeConverter
    fun fromDateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLongToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToString(date: Date?): String? {
        return date?.let { formatDate(date) }
    }


    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    @TypeConverter
    fun fromStringToDate(dateString: String?): Date? {
        return dateString?.let { dateFormat.parse(it) }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(date)
}