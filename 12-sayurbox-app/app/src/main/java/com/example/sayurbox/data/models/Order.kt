package com.example.sayurbox.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.sayurbox.data.database.Converters

@Entity(tableName = "orders")
@TypeConverters(Converters::class)
data class Order(
    @PrimaryKey
    val id: String, // Unique order ID (e.g., UUID)
    val userEmail: String, // Reference to User.email
    val items: List<CartItem>, // List of ordered items (converted to JSON)
    val total: Double,
    val timestamp: Long,
    val status: String = "completed"
)