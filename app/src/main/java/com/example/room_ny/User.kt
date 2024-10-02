package com.example.room_ny

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val firstName: String,
    val age: String,
    val surname: String = "Ansari",
    val createdDate: Date, // Storing date in long
    val updatedDate: Date // Storing date in String
)
