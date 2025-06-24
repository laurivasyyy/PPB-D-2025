// Simple OrderScreen.kt - Properly reactive
package com.example.sayurbox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sayurbox.R
import com.example.sayurbox.data.OrderManager
import com.example.sayurbox.data.SimpleOrder
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxLightGreen

@Composable
fun OrderScreen(
    modifier: Modifier = Modifier
) {
    // Direct access to OrderManager orders - this will automatically recompose
    val orders = OrderManager.orders

    // Force recomposition with a refresh button
    var refreshKey by remember { mutableStateOf(0) }

    // Create a derived state that depends on both orders and refresh key
    val currentOrders by remember(refreshKey) {
        derivedStateOf { OrderManager.orders }
    }

    LaunchedEffect(Unit) {
        println("OrderScreen: Current orders count: ${orders.size}")
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            SimpleOrderHeader(
                orderCount = currentOrders.size,
                onRefresh = {
                    refreshKey++
                    println("Refresh pressed - Orders: ${OrderManager.orders.size}")
                }
            )

            if (currentOrders.isEmpty()) {
                // Empty orders state
                EmptyOrdersState()
            } else {
                // Orders list
                Text(
                    text = "Found ${currentOrders.size} orders",
                    modifier = Modifier.padding(16.dp),
                    color = SayurboxGreen,
                    fontWeight = FontWeight.Medium
                )
                SimpleOrdersList(orders = currentOrders)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleOrderHeader(
    orderCount: Int,
    onRefresh: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.order_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "$orderCount order${if (orderCount != 1) "s" else ""}",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh orders",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SayurboxGreen
        )
    )
}

@Composable
private fun EmptyOrdersState() {
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
            // Empty orders icon
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
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = SayurboxGreen.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "No Orders Yet",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your orders will appear here after you place an order from your cart",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "💡 Steps to place an order:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SayurboxGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "1. Add items to cart\n2. Go to Cart tab\n3. Click 'Checkout Items'\n4. Confirm order",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun SimpleOrdersList(orders: List<SimpleOrder>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(orders) { order ->
            SimpleOrderCard(order = order)
        }
    }
}

@Composable
private fun SimpleOrderCard(order: SimpleOrder) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Order header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.id.take(8)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = order.date,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                // Status chip
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = SayurboxGreen.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Completed",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = SayurboxGreen,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order items
            Text(
                text = "Items (${order.items.size}):",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(order.items) { item ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                SayurboxLightGreen.copy(alpha = 0.3f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.emoji,
                                fontSize = 20.sp
                            )
                            if (item.quantity > 1) {
                                Text(
                                    text = "×${item.quantity}",
                                    fontSize = 10.sp,
                                    color = SayurboxGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Show item names
            Text(
                text = order.items.joinToString(", ") { "${it.name} ×${it.quantity}" },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Order footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Rp${String.format("%,.0f", order.total)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SayurboxGreen
                )

                OutlinedButton(
                    onClick = { /* TODO: Reorder */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SayurboxGreen
                    ),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Reorder",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}