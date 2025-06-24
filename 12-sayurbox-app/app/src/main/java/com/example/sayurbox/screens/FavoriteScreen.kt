package com.example.sayurbox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurbox.R
import com.example.sayurbox.ui.components.ProductCard
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxLightGreen
import com.example.sayurbox.ui.theme.SayurboxTheme
import com.example.sayurbox.viewmodel.CartViewModel
import com.example.sayurbox.viewmodel.ProductViewModel

@Composable
fun FavoriteScreen(
    onProductClick: (String) -> Unit,
    onNavigateToProducts: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val allProducts by productViewModel.allProducts.collectAsState(initial = emptyList())
    val favorites by productViewModel.favorites.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()

    val favoriteProducts = allProducts.filter { favorites.contains(it.id) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            FavoriteHeader(favoriteCount = favoriteProducts.size)

            if (isLoading) {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SayurboxGreen)
                }
            } else if (favoriteProducts.isEmpty()) {
                // Empty favorites state
                EmptyFavoritesState(onNavigateToProducts = onNavigateToProducts)
            } else {
                // Favorites grid
                FavoriteProductsGrid(
                    favoriteProducts = favoriteProducts,
                    favorites = favorites,
                    onProductClick = onProductClick,
                    onFavoriteClick = productViewModel::toggleFavorite,
                    onAddToCart = cartViewModel::addToCart
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteHeader(favoriteCount: Int) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.favorite_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (favoriteCount > 0) {
                    Text(
                        text = "$favoriteCount item${if (favoriteCount != 1) "s" else ""}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SayurboxGreen
        )
    )
}

@Composable
private fun EmptyFavoritesState(
    onNavigateToProducts: (() -> Unit)?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Empty favorites icon
            Card(
                modifier = Modifier.size(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SayurboxLightGreen.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = SayurboxGreen.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.no_favorites),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Start adding products to your favorites by tapping the heart icon",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            if (onNavigateToProducts != null) {
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onNavigateToProducts,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SayurboxGreen
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "Discover Products",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteProductsGrid(
    favoriteProducts: List<com.example.sayurbox.data.models.Product>,
    favorites: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (com.example.sayurbox.data.models.Product) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(favoriteProducts) { product ->
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

// Alternative favorites layout with categories
@Composable
private fun FavoriteProductsWithCategories(
    favoriteProducts: List<com.example.sayurbox.data.models.Product>,
    favorites: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (com.example.sayurbox.data.models.Product) -> Unit
) {
    val context = LocalContext.current
    val favoriteVegetables = favoriteProducts.filter {
        context.getString(it.categoryRes) == context.getString(R.string.category_vegetables)
    }
    val favoriteFruits = favoriteProducts.filter {
        context.getString(it.categoryRes) == context.getString(R.string.category_fruits)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vegetables section
        if (favoriteVegetables.isNotEmpty()) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Text(
                    text = "Favorite Vegetables",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SayurboxGreen,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(favoriteVegetables) { product ->
                ProductCard(
                    product = product,
                    isFavorite = true,
                    onProductClick = { onProductClick(product.id) },
                    onFavoriteClick = { onFavoriteClick(product.id) },
                    onAddToCart = { onAddToCart(product) }
                )
            }
        }

        // Fruits section
        if (favoriteFruits.isNotEmpty()) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Text(
                    text = "Favorite Fruits",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SayurboxGreen,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(favoriteFruits) { product ->
                ProductCard(
                    product = product,
                    isFavorite = true,
                    onProductClick = { onProductClick(product.id) },
                    onFavoriteClick = { onFavoriteClick(product.id) },
                    onAddToCart = { onAddToCart(product) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    SayurboxTheme {
        CartScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    SayurboxTheme {
        FavoriteScreen(
            onProductClick = {}
        )
    }
}