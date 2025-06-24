package com.example.sayurbox.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sayurbox.R
import com.example.sayurbox.screens.*
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxLightGreen
import com.example.sayurbox.ui.theme.SayurboxTheme
import com.example.sayurbox.viewmodel.CartViewModel
import com.example.sayurbox.viewmodel.ProductViewModel

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val cartItemCount by cartViewModel.cartItemCount.collectAsState(initial = 0)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MainBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                cartItemCount = cartItemCount
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> ProductsScreen(
                    onProductClick = { productId ->
                        navController.navigate("product_detail/$productId")
                    },
                    onViewAllClick = {
                        navController.navigate("all_products")
                    },
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel
                )

                1 -> CartScreen(
                    onNavigateToProducts = { selectedTab = 0 },
                    onNavigateToOrders = { selectedTab = 3 }, // Navigate to orders tab
                    cartViewModel = cartViewModel
                )

                2 -> FavoriteScreen(
                    onProductClick = { productId ->
                        navController.navigate("product_detail/$productId")
                    },
                    onNavigateToProducts = { selectedTab = 0 },
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel
                )

                3 -> OrderScreen()
            }
        }
    }
}

@Composable
private fun MainBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    cartItemCount: Int
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = SayurboxGreen
    ) {
        // Products tab
        NavigationBarItem(
            icon = {
                TabIcon(
                    selectedIcon = Icons.Filled.ShoppingBag,
                    unselectedIcon = Icons.Outlined.ShoppingBag,
                    isSelected = selectedTab == 0,
                    contentDescription = stringResource(R.string.nav_products)
                )
            },
            label = {
                TabLabel(
                    text = stringResource(R.string.nav_products),
                    isSelected = selectedTab == 0
                )
            },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SayurboxGreen,
                selectedTextColor = SayurboxGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = SayurboxLightGreen
            )
        )

        // Cart tab with badge
        NavigationBarItem(
            icon = {
                Box {
                    TabIcon(
                        selectedIcon = Icons.Filled.ShoppingCart,
                        unselectedIcon = Icons.Outlined.ShoppingCart,
                        isSelected = selectedTab == 1,
                        contentDescription = stringResource(R.string.nav_cart)
                    )

                    // Cart item count badge
                    if (cartItemCount > 0) {
                        Badge(
                            modifier = Modifier.offset(x = 12.dp, y = (-4).dp),
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ) {
                            Text(
                                text = if (cartItemCount > 99) "99+" else cartItemCount.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            label = {
                TabLabel(
                    text = stringResource(R.string.nav_cart),
                    isSelected = selectedTab == 1
                )
            },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SayurboxGreen,
                selectedTextColor = SayurboxGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = SayurboxLightGreen
            )
        )

        // Favorite tab
        NavigationBarItem(
            icon = {
                TabIcon(
                    selectedIcon = Icons.Filled.Favorite,
                    unselectedIcon = Icons.Outlined.Favorite,
                    isSelected = selectedTab == 2,
                    contentDescription = stringResource(R.string.nav_favorite)
                )
            },
            label = {
                TabLabel(
                    text = stringResource(R.string.nav_favorite),
                    isSelected = selectedTab == 2
                )
            },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SayurboxGreen,
                selectedTextColor = SayurboxGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = SayurboxLightGreen
            )
        )

        // Order tab
        NavigationBarItem(
            icon = {
                TabIcon(
                    selectedIcon = Icons.Filled.Receipt,
                    unselectedIcon = Icons.Outlined.Receipt,
                    isSelected = selectedTab == 3,
                    contentDescription = stringResource(R.string.nav_order)
                )
            },
            label = {
                TabLabel(
                    text = stringResource(R.string.nav_order),
                    isSelected = selectedTab == 3
                )
            },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SayurboxGreen,
                selectedTextColor = SayurboxGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = SayurboxLightGreen
            )
        )
    }
}

@Composable
private fun TabIcon(
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    contentDescription: String
) {
    Icon(
        imageVector = if (isSelected) selectedIcon else unselectedIcon,
        contentDescription = contentDescription,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
private fun TabLabel(
    text: String,
    isSelected: Boolean
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SayurboxTheme {
        MainScreen(navController = rememberNavController())
    }
}