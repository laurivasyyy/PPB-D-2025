package com.example.sayurbox.repository

import com.example.sayurbox.R
import com.example.sayurbox.data.database.AppDatabase
import com.example.sayurbox.data.models.CartItem
import com.example.sayurbox.data.models.Order
import com.example.sayurbox.data.models.Product
import com.example.sayurbox.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SayurboxRepository @Inject constructor(
    private val database: AppDatabase
) {

    // Sample data initialization
    suspend fun initializeSampleData() {
        // Check if products already exist
        val productCount = database.productDao().getProductCount()
        if (productCount == 0) {
            val sampleProducts = listOf(
                Product(
                    id = "1",
                    nameRes = R.string.product_carrot,
                    price = 15000.0,
                    priceRes = R.string.price_15k,
                    imageRes = "carrot",
                    categoryRes = R.string.category_vegetables,
                    descriptionRes = R.string.product_description
                ),
                Product(
                    id = "2",
                    nameRes = R.string.product_broccoli,
                    price = 15000.0,
                    priceRes = R.string.price_15k,
                    imageRes = "broccoli",
                    categoryRes = R.string.category_vegetables,
                    descriptionRes = R.string.product_description
                ),
                Product(
                    id = "3",
                    nameRes = R.string.product_eggplant,
                    price = 15000.0,
                    priceRes = R.string.price_15k,
                    imageRes = "eggplant",
                    categoryRes = R.string.category_vegetables,
                    descriptionRes = R.string.product_description
                ),
                Product(
                    id = "4",
                    nameRes = R.string.product_apple,
                    price = 12000.0,
                    priceRes = R.string.price_12k,
                    imageRes = "apple",
                    categoryRes = R.string.category_fruits,
                    descriptionRes = R.string.product_description
                ),
                Product(
                    id = "5",
                    nameRes = R.string.product_orange,
                    price = 18000.0,
                    priceRes = R.string.price_18k,
                    imageRes = "orange",
                    categoryRes = R.string.category_fruits,
                    descriptionRes = R.string.product_description
                ),
                Product(
                    id = "6",
                    nameRes = R.string.product_grape,
                    price = 22000.0,
                    priceRes = R.string.price_22k,
                    imageRes = "grape",
                    categoryRes = R.string.category_fruits,
                    descriptionRes = R.string.product_description
                )
            )
            database.productDao().insertProducts(sampleProducts)
        }
    }

    // Product operations
    fun getAllProducts(): Flow<List<Product>> = database.productDao().getAllProducts()

    fun getProductsByCategory(categoryRes: Int): Flow<List<Product>> =
        database.productDao().getProductsByCategory(categoryRes)

    suspend fun getProductById(id: String): Product? =
        database.productDao().getProductById(id)

    fun searchProducts(query: String): Flow<List<Product>> =
        database.productDao().searchProducts(query)

    suspend fun insertProduct(product: Product) =
        database.productDao().insertProduct(product)

    // Cart operations
    fun getCartItems(): Flow<List<CartItem>> = database.cartDao().getAllCartItems()

    fun getCartItemCount(): Flow<Int> = database.cartDao().getCartItemCount()

    fun getCartTotal(): Flow<Double?> = database.cartDao().getCartTotal()

    suspend fun addToCart(item: CartItem) = database.cartDao().addToCart(item)

    suspend fun addToCart(product: Product, quantity: Int = 1) {
        val existingItem = database.cartDao().getCartItemById(product.id)
        if (existingItem != null) {
            // Update quantity if item already exists
            val newQuantity = existingItem.quantity + quantity
            database.cartDao().updateQuantity(product.id, newQuantity)
        } else {
            // Add new item to cart
            val cartItem = CartItem(
                productId = product.id,
                quantity = quantity,
                product = product
            )
            database.cartDao().addToCart(cartItem)
        }
    }

    suspend fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            database.cartDao().removeFromCart(productId)
        } else {
            database.cartDao().updateQuantity(productId, quantity)
        }
    }

    suspend fun removeFromCart(productId: String) =
        database.cartDao().removeFromCart(productId)

    suspend fun clearCart() = database.cartDao().clearCart()

    suspend fun isProductInCart(productId: String): Boolean =
        database.cartDao().isProductInCart(productId)

    // User operations
    suspend fun login(email: String, password: String): User? =
        database.userDao().login(email, password)

    suspend fun register(user: User) = database.userDao().register(user)

    suspend fun getUserByEmail(email: String): User? =
        database.userDao().getUserByEmail(email)

    suspend fun isEmailExists(email: String): Boolean =
        database.userDao().isEmailExists(email)

    suspend fun updateUser(user: User) = database.userDao().updateUser(user)

    suspend fun updatePassword(email: String, newPassword: String) =
        database.userDao().updatePassword(email, newPassword)

    // Order operations
    fun getAllOrders(): Flow<List<Order>> = database.orderDao().getAllOrders()

    fun getOrdersByUser(userEmail: String): Flow<List<Order>> =
        database.orderDao().getOrdersByUser(userEmail)

    suspend fun getOrderById(orderId: String): Order? =
        database.orderDao().getOrderById(orderId)

    fun getOrdersByStatus(status: String): Flow<List<Order>> =
        database.orderDao().getOrdersByStatus(status)

    suspend fun getUserOrderCount(userEmail: String): Int =
        database.orderDao().getUserOrderCount(userEmail)

    suspend fun getUserTotalSpent(userEmail: String): Double? =
        database.orderDao().getUserTotalSpent(userEmail)

    suspend fun createOrder(userEmail: String, cartItems: List<CartItem>): Order {
        val total = cartItems.sumOf { it.quantity * it.product.price }
        val order = Order(
            id = UUID.randomUUID().toString(),
            userEmail = userEmail,
            items = cartItems,
            total = total,
            timestamp = System.currentTimeMillis(),
            status = "completed"
        )
        database.orderDao().insertOrder(order)
        return order
    }

    suspend fun insertOrder(order: Order) = database.orderDao().insertOrder(order)

    suspend fun updateOrderStatus(orderId: String, status: String) =
        database.orderDao().updateOrderStatus(orderId, status)

    suspend fun deleteOrder(orderId: String) =
        database.orderDao().deleteOrderById(orderId)

    // Updated checkout method
    suspend fun checkout(userEmail: String): Order? {
        return try {
            // Get current cart items
            val cartItems = getCartItems().first()

            if (cartItems.isEmpty()) {
                return null
            }

            // Create order
            val order = createOrder(userEmail, cartItems)

            // Clear cart after successful order
            clearCart()

            order
        } catch (e: Exception) {
            null
        }
    }
}