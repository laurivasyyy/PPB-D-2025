package com.example.sayurbox.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.sayurbox.data.models.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE categoryRes = :categoryRes ORDER BY id ASC")
    fun getProductsByCategory(categoryRes: Int): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): Product?

    // Enhanced search query - searches in multiple fields
    @Query("""
        SELECT * FROM products 
        WHERE id LIKE '%' || :searchQuery || '%' 
        OR imageRes LIKE '%' || :searchQuery || '%'
        ORDER BY 
        CASE 
            WHEN imageRes LIKE :searchQuery || '%' THEN 1
            WHEN imageRes LIKE '%' || :searchQuery || '%' THEN 2
            ELSE 3
        END, id ASC
    """)
    fun searchProducts(searchQuery: String): Flow<List<Product>>

    // Search by exact image resource (for emoji-based search)
    @Query("SELECT * FROM products WHERE imageRes = :imageRes ORDER BY id ASC")
    fun searchByImageRes(imageRes: String): Flow<List<Product>>

    // Search by category resource
    @Query("SELECT * FROM products WHERE categoryRes = :categoryRes ORDER BY id ASC")
    fun searchByCategory(categoryRes: Int): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    // Get products by price range
    @Query("SELECT * FROM products WHERE price BETWEEN :minPrice AND :maxPrice ORDER BY price ASC")
    fun getProductsByPriceRange(minPrice: Double, maxPrice: Double): Flow<List<Product>>

    // Get products sorted by price
    @Query("SELECT * FROM products ORDER BY price ASC")
    fun getProductsSortedByPriceAsc(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY price DESC")
    fun getProductsSortedByPriceDesc(): Flow<List<Product>>
}