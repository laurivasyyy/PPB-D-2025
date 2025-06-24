package com.example.sayurbox.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sayurbox.R
import com.example.sayurbox.data.models.Product
import com.example.sayurbox.ui.theme.SayurboxDarkGreen
import com.example.sayurbox.ui.theme.SayurboxGreen

@Composable
fun CategorySection(
    title: String,
    products: List<Product>,
    favorites: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (Product) -> Unit,
    onViewAllClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Header with title and "View all" button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SayurboxDarkGreen
            )

            if (onViewAllClick != null) {
                TextButton(
                    onClick = onViewAllClick
                ) {
                    Text(
                        text = stringResource(R.string.view_all),
                        fontSize = 14.sp,
                        color = SayurboxGreen
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.view_all),
                    fontSize = 14.sp,
                    color = SayurboxGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Product cards in horizontal scroll
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No products available",
                    fontSize = 14.sp,
                    color = SayurboxGreen
                )
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = favorites.contains(product.id),
                        onProductClick = { onProductClick(product.id) },
                        onFavoriteClick = { onFavoriteClick(product.id) },
                        onAddToCart = { onAddToCart(product) }
                    )
                }
            }
        }
    }
}

// Additional helper composables
@Composable
fun ProductGrid(
    products: List<Product>,
    favorites: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 2
) {
    if (products.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No products found",
                fontSize = 16.sp,
                color = SayurboxGreen
            )
        }
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            products.chunked(columns).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(
                            product = product,
                            isFavorite = favorites.contains(product.id),
                            onProductClick = { onProductClick(product.id) },
                            onFavoriteClick = { onFavoriteClick(product.id) },
                            onAddToCart = { onAddToCart(product) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill remaining space if row is not complete
                    repeat(columns - rowProducts.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}