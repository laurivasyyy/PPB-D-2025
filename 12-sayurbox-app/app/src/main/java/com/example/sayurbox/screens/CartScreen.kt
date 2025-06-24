package com.example.sayurbox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.sayurbox.ui.components.CartItemCard
import com.example.sayurbox.ui.theme.SayurboxDarkGreen
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxLightGreen
import com.example.sayurbox.ui.theme.SayurboxTheme
import com.example.sayurbox.viewmodel.CartViewModel
import kotlinx.coroutines.delay

@Composable
fun CartScreen(
    onNavigateToProducts: (() -> Unit)? = null,
    onNavigateToOrders: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val cartTotal by cartViewModel.cartTotal.collectAsState(initial = 0.0)
    val cartItemCount by cartViewModel.cartItemCount.collectAsState(initial = 0)
    val isLoading by cartViewModel.isLoading.collectAsState()
    val checkoutStatus by cartViewModel.checkoutStatus.collectAsState()
    val context = LocalContext.current

    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showClearCartDialog by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Handle checkout result
    LaunchedEffect(checkoutStatus) {
        when (checkoutStatus) {
            is CartViewModel.CheckoutStatus.Success -> {
                showCheckoutDialog = false
                showSuccessMessage = true
                // Auto-hide success message after 3 seconds
                delay(3000)
                showSuccessMessage = false
                cartViewModel.resetCheckoutStatus()
            }
            is CartViewModel.CheckoutStatus.Error -> {
                showCheckoutDialog = false
                // You could show error message here
            }
            else -> {}
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                CartHeader(
                    itemCount = cartItemCount,
                    onClearCart = {
                        if (cartItems.isNotEmpty()) {
                            showClearCartDialog = true
                        }
                    }
                )

                if (isLoading) {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SayurboxGreen)
                    }
                } else if (cartItems.isEmpty()) {
                    // Empty cart state
                    EmptyCartState(onNavigateToProducts = onNavigateToProducts)
                } else {
                    // Cart items
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(8.dp)) }

                        items(cartItems) { cartItem ->
                            CartItemCard(
                                cartItem = cartItem,
                                onQuantityChange = { newQuantity ->
                                    cartViewModel.updateQuantity(cartItem.productId, newQuantity)
                                },
                                onRemove = {
                                    cartViewModel.removeFromCart(cartItem.productId)
                                }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }

                    // Checkout section
                    CheckoutSection(
                        itemCount = cartItemCount,
                        total = cartTotal ?: 0.0,
                        onCheckout = { showCheckoutDialog = true },
                        isLoading = checkoutStatus is CartViewModel.CheckoutStatus.Loading
                    )
                }
            }

            // Success message overlay
            if (showSuccessMessage) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SuccessMessageCard(
                        onViewOrders = onNavigateToOrders
                    )
                }
            }
        }

        // Checkout confirmation dialog
        if (showCheckoutDialog) {
            CheckoutDialog(
                itemCount = cartItemCount,
                total = cartTotal ?: 0.0,
                onConfirm = {
                    // FIXED: Direct checkout call
                    cartViewModel.checkout()
                },
                onDismiss = { showCheckoutDialog = false },
                isLoading = checkoutStatus is CartViewModel.CheckoutStatus.Loading
            )
        }

        // Clear cart confirmation dialog
        if (showClearCartDialog) {
            AlertDialog(
                onDismissRequest = { showClearCartDialog = false },
                title = { Text("Clear Cart") },
                text = { Text("Are you sure you want to remove all items from your cart?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            cartViewModel.clearCart()
                            showClearCartDialog = false
                        }
                    ) {
                        Text("Clear", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearCartDialog = false }) {
                        Text("Cancel", color = SayurboxGreen)
                    }
                }
            )
        }
    }
}

@Composable
private fun SuccessMessageCard(
    onViewOrders: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SayurboxGreen
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Order Placed Successfully!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your order has been confirmed and will be delivered soon.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            if (onViewOrders != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onViewOrders,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = SayurboxGreen
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "View Orders",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartHeader(
    itemCount: Int,
    onClearCart: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.cart_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        actions = {
            if (itemCount > 0) {
                IconButton(onClick = onClearCart) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear cart",
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

@Composable
private fun EmptyCartState(
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
            // Empty cart icon
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
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = SayurboxGreen.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.cart_empty),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add some fresh vegetables and fruits to get started!",
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
                        text = "Browse Products",
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
private fun CheckoutSection(
    itemCount: Int,
    total: Double,
    onCheckout: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SayurboxGreen
        ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Order summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total ($itemCount items)",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Rp${String.format("%,.0f", total)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Checkout button
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && itemCount > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SayurboxLightGreen,
                    contentColor = SayurboxDarkGreen
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = SayurboxDarkGreen,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.checkout_items),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckoutDialog(
    itemCount: Int,
    total: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text(
                text = "Confirm Order",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("Are you sure you want to place this order?")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Items: $itemCount",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = "Total: Rp${String.format("%,.0f", total)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SayurboxGreen
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SayurboxGreen
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Place Order")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CartScreenFixedPreview() {
    SayurboxTheme {
        CartScreen()
    }
}