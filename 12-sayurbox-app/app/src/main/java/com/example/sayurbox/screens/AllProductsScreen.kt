package com.example.sayurbox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurbox.R
import com.example.sayurbox.ui.components.ProductCard
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxTheme
import com.example.sayurbox.viewmodel.CartViewModel
import com.example.sayurbox.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductsScreen(
    onBackPress: () -> Unit,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val allProducts by productViewModel.allProducts.collectAsState(initial = emptyList())
    val favorites by productViewModel.favorites.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val searchQuery by productViewModel.searchQuery.collectAsState()
    val searchResults by productViewModel.searchResults.collectAsState()

    var selectedCategory by remember { mutableStateOf("all") }
    var showSearchBar by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Filter products based on selected category
    val filteredProducts = remember(allProducts, selectedCategory, context) {
        when (selectedCategory) {
            "vegetables" -> allProducts.filter {
                context.getString(it.categoryRes) == context.getString(R.string.category_vegetables)
            }
            "fruits" -> allProducts.filter {
                context.getString(it.categoryRes) == context.getString(R.string.category_fruits)
            }
            else -> allProducts
        }
    }

    // Use search results if searching, otherwise use filtered products
    val displayProducts = if (searchQuery.isNotEmpty()) searchResults else filteredProducts

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            AllProductsHeader(
                selectedCategory = selectedCategory,
                showSearchBar = showSearchBar,
                searchQuery = searchQuery,
                onBackPress = onBackPress,
                onSearchToggle = { showSearchBar = !showSearchBar },
                onFilterClick = { showFilterSheet = true },
                onSearchQueryChange = productViewModel::searchProducts,
                onClearSearch = {
                    productViewModel.clearSearch()
                    showSearchBar = false
                }
            )

            // Category filter chips
            if (!showSearchBar) {
                CategoryFilterChips(
                    selectedCategory = selectedCategory,
                    onCategoryChange = { selectedCategory = it }
                )
            }

            if (isLoading) {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SayurboxGreen)
                }
            } else {
                // Products grid
                ProductsGrid(
                    products = displayProducts,
                    favorites = favorites,
                    onProductClick = onProductClick,
                    onFavoriteClick = productViewModel::toggleFavorite,
                    onAddToCart = cartViewModel::addToCart,
                    searchQuery = searchQuery
                )
            }
        }
    }

    // Filter bottom sheet
    if (showFilterSheet) {
        FilterBottomSheet(
            selectedCategory = selectedCategory,
            onCategoryChange = { selectedCategory = it },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllProductsHeader(
    selectedCategory: String,
    showSearchBar: Boolean,
    searchQuery: String,
    onBackPress: () -> Unit,
    onSearchToggle: () -> Unit,
    onFilterClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                if (showSearchBar) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search products...", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = when (selectedCategory) {
                            "vegetables" -> "Vegetables"
                            "fruits" -> "Fruits"
                            else -> stringResource(R.string.products_title)
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = if (showSearchBar) onClearSearch else onBackPress) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = if (showSearchBar) "Clear search" else stringResource(R.string.cd_back),
                        tint = Color.White
                    )
                }
            },
            actions = {
                if (!showSearchBar) {
                    IconButton(onClick = onSearchToggle) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onFilterClick) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = SayurboxGreen
            )
        )
    }
}

@Composable
private fun CategoryFilterChips(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CategoryChip(
            text = "All",
            isSelected = selectedCategory == "all",
            onClick = { onCategoryChange("all") }
        )
        CategoryChip(
            text = "Vegetables",
            isSelected = selectedCategory == "vegetables",
            onClick = { onCategoryChange("vegetables") }
        )
        CategoryChip(
            text = "Fruits",
            isSelected = selectedCategory == "fruits",
            onClick = { onCategoryChange("fruits") }
        )
    }
}

@Composable
private fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        label = { Text(text) },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SayurboxGreen,
            selectedLabelColor = Color.White,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun ProductsGrid(
    products: List<com.example.sayurbox.data.models.Product>,
    favorites: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (com.example.sayurbox.data.models.Product) -> Unit,
    searchQuery: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (products.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            "No products found for \"$searchQuery\""
                        } else {
                            "No products available"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                    if (searchQuery.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try adjusting your search terms",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = favorites.contains(product.id),
                        onProductClick = { onProductClick(product.id) },
                        onFavoriteClick = { onFavoriteClick(product.id) },
                        onAddToCart = { onAddToCart(product) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(true) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                onDismiss()
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Filter Products",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Category",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category options
                val categories = listOf(
                    "all" to "All Products",
                    "vegetables" to "Vegetables",
                    "fruits" to "Fruits"
                )

                categories.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedCategory == value,
                            onClick = { onCategoryChange(value) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = SayurboxGreen
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = label,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        showBottomSheet = false
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SayurboxGreen
                    )
                ) {
                    Text("Apply Filter")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    SayurboxTheme {
        OrderScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AllProductsScreenPreview() {
    SayurboxTheme {
        AllProductsScreen(
            onBackPress = {},
            onProductClick = {}
        )
    }
}

private fun formatOrderDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}