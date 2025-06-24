// Updated ProductsScreen.kt with Prominent Search
package com.example.sayurbox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurbox.R
import com.example.sayurbox.ui.components.CategorySection
import com.example.sayurbox.ui.components.ProductGrid
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxTheme
import com.example.sayurbox.viewmodel.CartViewModel
import com.example.sayurbox.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onProductClick: (String) -> Unit,
    onViewAllClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val products by productViewModel.allProducts.collectAsState(initial = emptyList())
    val favorites by productViewModel.favorites.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val searchQuery by productViewModel.searchQuery.collectAsState()
    val searchResults by productViewModel.searchResults.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        ProductsHeader()

        // Search Bar - Always visible and prominent
        SearchSection(
            searchQuery = searchQuery,
            onSearchQueryChange = productViewModel::searchProducts,
            onClearSearch = {
                productViewModel.clearSearch()
                focusManager.clearFocus()
            }
        )

        if (isLoading) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = SayurboxGreen
                )
            }
        } else if (searchQuery.isNotEmpty()) {
            // Search results
            SearchResultsSection(
                searchQuery = searchQuery,
                searchResults = searchResults,
                favorites = favorites,
                onProductClick = onProductClick,
                onFavoriteClick = productViewModel::toggleFavorite,
                onAddToCart = cartViewModel::addToCart,
                onClearSearch = {
                    productViewModel.clearSearch()
                    focusManager.clearFocus()
                }
            )
        } else {
            // Main products view
            MainProductsContent(
                products = products,
                favorites = favorites,
                onProductClick = onProductClick,
                onFavoriteClick = productViewModel::toggleFavorite,
                onAddToCart = cartViewModel::addToCart,
                onViewAllClick = onViewAllClick,
                context = context
            )
        }
    }
}

@Composable
private fun ProductsHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SayurboxGreen)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.products_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    "Search for vegetables, fruits...",
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = SayurboxGreen
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = onClearSearch) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = Color.Gray
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SayurboxGreen,
                focusedLabelColor = SayurboxGreen,
                unfocusedBorderColor = Color.Transparent,
                unfocusedLabelColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Search is already happening in real-time
                    // This just dismisses the keyboard
                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun SearchResultsSection(
    searchQuery: String,
    searchResults: List<com.example.sayurbox.data.models.Product>,
    favorites: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (com.example.sayurbox.data.models.Product) -> Unit,
    onClearSearch: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Search results header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Search Results",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SayurboxGreen
                    )
                    Text(
                        text = "\"$searchQuery\" • ${searchResults.size} found",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                TextButton(onClick = onClearSearch) {
                    Text(
                        text = "Clear",
                        color = SayurboxGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (searchResults.isEmpty()) {
            item {
                // Empty search results
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔍",
                            fontSize = 48.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No products found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Try searching for 'carrot', 'apple', or 'broccoli'",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(onClick = onClearSearch) {
                            Text(
                                text = "Browse all products",
                                color = SayurboxGreen
                            )
                        }
                    }
                }
            }
        } else {
            item {
                // Search results grid
                ProductGrid(
                    products = searchResults,
                    favorites = favorites,
                    onProductClick = onProductClick,
                    onFavoriteClick = onFavoriteClick,
                    onAddToCart = onAddToCart,
                    columns = 2
                )
            }
        }
    }
}

@Composable
private fun MainProductsContent(
    products: List<com.example.sayurbox.data.models.Product>,
    favorites: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (com.example.sayurbox.data.models.Product) -> Unit,
    onViewAllClick: (() -> Unit)?,
    context: android.content.Context
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Quick search suggestions
        item {
            QuickSearchSuggestions()
        }

        // Vegetables section
        val vegetables = products.filter {
            context.getString(it.categoryRes) == context.getString(R.string.category_vegetables)
        }
        if (vegetables.isNotEmpty()) {
            item {
                CategorySection(
                    title = stringResource(R.string.vegetables_deals),
                    products = vegetables,
                    favorites = favorites,
                    onProductClick = onProductClick,
                    onFavoriteClick = onFavoriteClick,
                    onAddToCart = onAddToCart,
                    onViewAllClick = onViewAllClick
                )
            }
        }

        // Fruits section
        val fruits = products.filter {
            context.getString(it.categoryRes) == context.getString(R.string.category_fruits)
        }
        if (fruits.isNotEmpty()) {
            item {
                CategorySection(
                    title = stringResource(R.string.fruits_deals),
                    products = fruits,
                    favorites = favorites,
                    onProductClick = onProductClick,
                    onFavoriteClick = onFavoriteClick,
                    onAddToCart = onAddToCart,
                    onViewAllClick = onViewAllClick
                )
            }
        }

        // Empty state
        if (products.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No products available",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please check back later",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickSearchSuggestions() {
    Column {
        Text(
            text = "Popular Searches",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = SayurboxGreen,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = listOf("🥕 Carrot", "🍎 Apple", "🥦 Broccoli", "🍇 Grape")

            suggestions.forEach { suggestion ->
                SuggestionChip(
                    onClick = { /* Could implement quick search here */ },
                    label = {
                        Text(
                            text = suggestion,
                            fontSize = 12.sp
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = SayurboxGreen.copy(alpha = 0.1f),
                        labelColor = SayurboxGreen
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenSearchPreview() {
    SayurboxTheme {
        ProductsScreen(
            onProductClick = {}
        )
    }
}