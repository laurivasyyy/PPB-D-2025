package com.example.sayurbox.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurbox.data.models.Product
import com.example.sayurbox.repository.SayurboxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: SayurboxRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val allProducts = repository.getAllProducts()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    init {
        // Initialize sample data when ViewModel is created
        viewModelScope.launch {
            repository.initializeSampleData()
        }

        // Set up search with debouncing
        setupSearch()
    }

    private fun setupSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .distinctUntilChanged() // Only search if query actually changed
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(emptyList())
                    } else {
                        performSearch(query)
                    }
                }
                .collect { results ->
                    _searchResults.value = results
                }
        }
    }

    private suspend fun performSearch(query: String): kotlinx.coroutines.flow.Flow<List<Product>> {
        return try {
            // Get all products and filter by name (using string resources)
            repository.getAllProducts().flatMapLatest { products ->
                val filteredProducts = products.filter { product ->
                    val productName = context.getString(product.nameRes).lowercase()
                    val searchTerm = query.lowercase().trim()

                    // Search in product name
                    productName.contains(searchTerm) ||
                            // Also search in category
                            context.getString(product.categoryRes).lowercase().contains(searchTerm) ||
                            // Search by emoji representation
                            getProductSearchTerms(product.imageRes).any { it.contains(searchTerm) }
                }
                flowOf(filteredProducts)
            }
        } catch (e: Exception) {
            flowOf(emptyList())
        }
    }

    private fun getProductSearchTerms(imageRes: String): List<String> {
        // Additional search terms for better discoverability
        return when (imageRes) {
            "carrot" -> listOf("carrot", "orange", "vegetable", "root")
            "broccoli" -> listOf("broccoli", "green", "vegetable", "tree")
            "eggplant" -> listOf("eggplant", "aubergine", "purple", "vegetable")
            "apple" -> listOf("apple", "red", "fruit", "sweet")
            "orange" -> listOf("orange", "citrus", "fruit", "vitamin")
            "grape" -> listOf("grape", "purple", "fruit", "wine")
            else -> listOf(imageRes)
        }
    }

    fun selectProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedProduct.value = repository.getProductById(productId)
            } catch (e: Exception) {
                // Handle error - could emit to error state
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(productId: String) {
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        _favorites.value = currentFavorites
    }

    fun getFavoriteProducts(): List<Product> {
        // This would typically be observed via Flow, but for simplicity:
        // You'd collect allProducts and filter by favorites
        return emptyList() // Implement based on your needs
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun getProductsByCategory(categoryRes: Int) =
        repository.getProductsByCategory(categoryRes)

    fun refreshProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Could add refresh logic here
                repository.initializeSampleData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Quick search suggestions
    fun getPopularSearchTerms(): List<String> {
        return listOf("carrot", "apple", "broccoli", "grape", "orange", "eggplant")
    }

    // Search by category
    fun searchByCategory(category: String) {
        when (category.lowercase()) {
            "vegetables", "vegetable" -> {
                searchProducts("vegetable")
            }
            "fruits", "fruit" -> {
                searchProducts("fruit")
            }
            else -> {
                searchProducts(category)
            }
        }
    }
}