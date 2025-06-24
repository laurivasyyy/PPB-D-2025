package com.example.sayurbox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
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
import com.example.sayurbox.ui.components.getProductEmoji
import com.example.sayurbox.ui.theme.SayurboxDarkGreen
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxLightGreen
import com.example.sayurbox.ui.theme.SayurboxTheme
import com.example.sayurbox.viewmodel.CartViewModel
import com.example.sayurbox.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val selectedProduct by productViewModel.selectedProduct.collectAsState()
    val favorites by productViewModel.favorites.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    var quantity by remember { mutableStateOf(1) }
    var showAddedToCart by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(productId) {
        productViewModel.selectProduct(productId)
    }

    // Show snackbar when item is added to cart
    LaunchedEffect(showAddedToCart) {
        if (showAddedToCart) {
            kotlinx.coroutines.delay(2000)
            showAddedToCart = false
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoading || selectedProduct == null) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = SayurboxGreen
                )
            }
        } else {
            selectedProduct?.let { product ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header with back button and favorite
                    ProductDetailHeader(
                        product = product,
                        isFavorite = favorites.contains(product.id),
                        onBackPress = onBackPress,
                        onFavoriteClick = { productViewModel.toggleFavorite(product.id) }
                    )

                    // Product content
                    ProductDetailContent(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = { quantity = it },
                        onAddToCart = {
                            cartViewModel.addToCart(product, quantity)
                            showAddedToCart = true
                        }
                    )
                }

                // Success message overlay
                if (showAddedToCart) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = SayurboxGreen
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                text = "Added to cart! 🛒",
                                modifier = Modifier.padding(16.dp),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailHeader(
    product: com.example.sayurbox.data.models.Product,
    isFavorite: Boolean,
    onBackPress: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.products_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.cd_favorite),
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SayurboxGreen
        )
    )
}

@Composable
private fun ProductDetailContent(
    product: com.example.sayurbox.data.models.Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        // Product image
        ProductImage(imageRes = product.imageRes)

        Spacer(modifier = Modifier.height(24.dp))

        // Product info
        ProductInfo(product = product)

        Spacer(modifier = Modifier.height(32.dp))

        // Quantity selector
        QuantitySelector(
            quantity = quantity,
            onQuantityChange = onQuantityChange
        )

        Spacer(modifier = Modifier.weight(1f))

        // Add to cart button
        AddToCartButton(
            onClick = onAddToCart,
            enabled = quantity > 0
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProductImage(imageRes: String) {
    Card(
        modifier = Modifier.size(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = SayurboxLightGreen.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getProductEmoji(imageRes),
                fontSize = 80.sp
            )
        }
    }
}

@Composable
private fun ProductInfo(product: com.example.sayurbox.data.models.Product) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(product.nameRes),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(product.priceRes),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = SayurboxGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(product.descriptionRes),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Decrease button
        IconButton(
            onClick = {
                if (quantity > 1) onQuantityChange(quantity - 1)
            },
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (quantity > 1) SayurboxLightGreen else SayurboxLightGreen.copy(alpha = 0.3f),
                    CircleShape
                ),
            enabled = quantity > 1
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = stringResource(R.string.cd_decrease),
                tint = if (quantity > 1) SayurboxDarkGreen else SayurboxDarkGreen.copy(alpha = 0.3f),
                modifier = Modifier.size(24.dp)
            )
        }

        // Quantity display
        Card(
            colors = CardDefaults.cardColors(
                containerColor = SayurboxGreen.copy(alpha = 0.1f)
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = quantity.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SayurboxDarkGreen,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        // Increase button
        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier
                .size(48.dp)
                .background(SayurboxLightGreen, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.cd_increase),
                tint = SayurboxDarkGreen,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AddToCartButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = SayurboxGreen,
            disabledContainerColor = SayurboxGreen.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = stringResource(R.string.add_to_cart),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview() {
    SayurboxTheme {
        ProductsScreen(
            onProductClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    SayurboxTheme {
        ProductDetailScreen(
            productId = "1",
            onBackPress = {}
        )
    }
}
