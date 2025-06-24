package com.example.sayurbox.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.sayurbox.data.models.Order

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    fun getOrdersByUser(userEmail: String): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): Order?

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY timestamp DESC")
    fun getOrdersByStatus(status: String): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE userEmail = :userEmail AND status = :status ORDER BY timestamp DESC")
    fun getUserOrdersByStatus(userEmail: String, status: String): Flow<List<Order>>

    @Query("SELECT COUNT(*) FROM orders WHERE userEmail = :userEmail")
    suspend fun getUserOrderCount(userEmail: String): Int

    @Query("SELECT SUM(total) FROM orders WHERE userEmail = :userEmail AND status = 'completed'")
    suspend fun getUserTotalSpent(userEmail: String): Double?

    @Query("SELECT * FROM orders WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getOrdersByDateRange(startTime: Long, endTime: Long): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun updateOrder(order: Order)

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: String)

    @Query("DELETE FROM orders WHERE userEmail = :userEmail")
    suspend fun deleteUserOrders(userEmail: String)

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
}