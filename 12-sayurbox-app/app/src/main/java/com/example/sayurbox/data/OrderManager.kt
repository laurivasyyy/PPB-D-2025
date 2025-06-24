package com.example.sayurbox.data

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.*

// Simple Order data class
data class SimpleOrder(
    val id: String = UUID.randomUUID().toString(),
    val items: List<SimpleOrderItem>,
    val total: Double,
    val date: String = getCurrentDate(),
    val status: String = "completed"
)

data class SimpleOrderItem(
    val name: String,
    val emoji: String,
    val quantity: Int,
    val price: Double
)

// Simple Order Manager - Like a global note list
object OrderManager {
    private val _orders = mutableStateListOf<SimpleOrder>()
    val orders: List<SimpleOrder> get() = _orders.toList()

    fun addOrder(order: SimpleOrder) {
        _orders.add(0, order) // Add to beginning for newest first
    }

    fun clearOrders() {
        _orders.clear()
    }

    fun getOrderCount(): Int = _orders.size
}

private fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
    return sdf.format(Date())
}