package com.example.sayurbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurbox.data.models.Order
import com.example.sayurbox.repository.SayurboxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: SayurboxRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadAllOrders()
    }

    private fun loadAllOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllOrders().collect { orderList ->
                    _orders.value = orderList.sortedByDescending { it.timestamp }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load orders: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserOrders(userEmail: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getOrdersByUser(userEmail).collect { orderList ->
                    _orders.value = orderList.sortedByDescending { it.timestamp }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load user orders: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshOrders() {
        loadAllOrders()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}