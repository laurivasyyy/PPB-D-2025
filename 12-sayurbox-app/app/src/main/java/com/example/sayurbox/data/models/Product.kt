// Product.kt
package com.example.sayurbox.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String,
    val nameRes: Int,
    val price: Double,
    val priceRes: Int,
    val imageRes: String,
    val categoryRes: Int,
    val descriptionRes: Int
)