// CartItem.kt
package com.example.sayurbox.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: String,
    val quantity: Int,
    @Embedded
    val product: Product
)