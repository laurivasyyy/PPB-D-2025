// Fixed CartViewModel.kt with working simple checkout
package com.example.sayurbox.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurbox.R
import com.example.sayurbox.data.OrderManager
import com.example.sayurbox.data.SimpleOrder
import com.example.sayurbox.data.SimpleOrderItem
import com.example.sayurbox.data.models.CartItem
import com.example.sayurbox.data.models.Product
import com.example.sayurbox.repository.SayurboxRepository
import com.example.sayurbox.ui.components.getProductEmoji
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: SayurboxRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val cartItems = repository.getCartItems()
    val cartItemCount = repository.getCartItemCount()
    val cartTotal = repository.getCartTotal()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _checkoutStatus = MutableStateFlow<CheckoutStatus>(CheckoutStatus.Idle)
    val checkoutStatus: StateFlow<CheckoutStatus> = _checkoutStatus.asStateFlow()

    sealed class CheckoutStatus {
        object Idle : CheckoutStatus()
        object Loading : CheckoutStatus()
        object Success : CheckoutStatus()
        data class Error(val message: String) : CheckoutStatus()
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                if (quantity <= 0) {
                    _errorMessage.value = "Quantity must be greater than 0"
                    return@launch
                }

                repository.addToCart(product, quantity)
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Failed to add item to cart: ${e.message}"
            }
        }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                if (quantity < 0) {
                    _errorMessage.value = "Quantity cannot be negative"
                    return@launch
                }

                repository.updateCartQuantity(productId, quantity)
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Failed to update quantity: ${e.message}"
            }
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            try {
                repository.removeFromCart(productId)
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Failed to remove item: ${e.message}"
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                repository.clearCart()
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Failed to clear cart: ${e.message}"
            }
        }
    }

    fun increaseQuantity(productId: String) {
        viewModelScope.launch {
            try {
                val currentItems = cartItems.first()
                val currentItem = currentItems.find { it.productId == productId }
                currentItem?.let { item ->
                    updateQuantity(productId, item.quantity + 1)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to increase quantity: ${e.message}"
            }
        }
    }

    fun decreaseQuantity(productId: String) {
        viewModelScope.launch {
            try {
                val currentItems = cartItems.first()
                val currentItem = currentItems.find { it.productId == productId }
                currentItem?.let { item ->
                    val newQuantity = item.quantity - 1
                    if (newQuantity <= 0) {
                        removeFromCart(productId)
                    } else {
                        updateQuantity(productId, newQuantity)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to decrease quantity: ${e.message}"
            }
        }
    }

    // SIMPLE CHECKOUT - Just add to order list!
    fun checkout() {
        viewModelScope.launch {
            println("Checkout started") // Debug log
            _checkoutStatus.value = CheckoutStatus.Loading

            try {
                // Get current cart items
                val currentCartItems = cartItems.first()
                println("Cart items: ${currentCartItems.size}") // Debug log

                if (currentCartItems.isEmpty()) {
                    println("Cart is empty") // Debug log
                    _checkoutStatus.value = CheckoutStatus.Error("Cart is empty")
                    return@launch
                }

                // Convert cart items to simple order items
                val orderItems = currentCartItems.map { cartItem ->
                    SimpleOrderItem(
                        name = context.getString(cartItem.product.nameRes),
                        emoji = getProductEmoji(cartItem.product.imageRes),
                        quantity = cartItem.quantity,
                        price = cartItem.product.price
                    )
                }

                // Calculate total
                val total = currentCartItems.sumOf { it.quantity * it.product.price }
                println("Order total: $total") // Debug log

                // Create simple order
                val order = SimpleOrder(
                    items = orderItems,
                    total = total
                )

                println("Adding order to OrderManager") // Debug log
                // Add to OrderManager (like adding a note!)
                OrderManager.addOrder(order)

                println("Clearing cart") // Debug log
                // Clear cart
                repository.clearCart()

                println("Checkout successful") // Debug log
                _checkoutStatus.value = CheckoutStatus.Success

            } catch (e: Exception) {
                println("Checkout failed: ${e.message}") // Debug log
                _checkoutStatus.value = CheckoutStatus.Error("Checkout failed: ${e.message}")
            }
        }
    }

    suspend fun isProductInCart(productId: String): Boolean {
        return repository.isProductInCart(productId)
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun resetCheckoutStatus() {
        _checkoutStatus.value = CheckoutStatus.Idle
    }
}