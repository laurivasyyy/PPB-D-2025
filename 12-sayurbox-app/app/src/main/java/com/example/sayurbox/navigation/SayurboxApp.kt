package com.example.sayurbox.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sayurbox.screens.*
import com.example.sayurbox.ui.theme.SayurboxTheme
import com.example.sayurbox.viewmodel.AuthViewModel
import com.example.sayurbox.viewmodel.CartViewModel
import com.example.sayurbox.viewmodel.ProductViewModel

@Composable
fun SayurboxApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val productViewModel: ProductViewModel = hiltViewModel()
    val cartViewModel: CartViewModel = hiltViewModel()

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    SayurboxTheme {
        Scaffold(
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) "main" else "welcome",
                modifier = Modifier.padding(innerPadding)
            ) {
                // Authentication flow
                composable("welcome") {
                    WelcomeScreen(
                        onNavigateToSignUp = {
                            navController.navigate("signup") {
                                launchSingleTop = true
                            }
                        },
                        onNavigateToSignIn = {
                            navController.navigate("signin") {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable("signup") {
                    SignUpScreen(
                        onNavigateToSignIn = {
                            navController.navigate("signin") {
                                popUpTo("welcome") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToMain = {
                            navController.navigate("main") {
                                popUpTo("welcome") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        viewModel = authViewModel
                    )
                }

                composable("signin") {
                    SignInScreen(
                        onNavigateToMain = {
                            navController.navigate("main") {
                                popUpTo("welcome") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToSignUp = {
                            navController.navigate("signup") {
                                popUpTo("welcome") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        viewModel = authViewModel
                    )
                }

                // Main app flow
                composable("main") {
                    MainScreen(
                        navController = navController,
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel
                    )
                }

                // Product detail screen
                composable("product_detail/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetailScreen(
                        productId = productId,
                        onBackPress = {
                            navController.popBackStack()
                        },
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel
                    )
                }

                // All products screen
                composable("all_products") {
                    AllProductsScreen(
                        onBackPress = {
                            navController.popBackStack()
                        },
                        onProductClick = { productId ->
                            navController.navigate("product_detail/$productId")
                        },
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel
                    )
                }

                // Settings or profile screen (optional)
                composable("profile") {
                    ProfileScreen(
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate("welcome") {
                                popUpTo("main") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

// Optional Profile Screen
@Composable
private fun ProfileScreen(
    onLogout: () -> Unit,
    authViewModel: AuthViewModel
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Text(
            text = "Profile",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        currentUser?.let { user ->
            androidx.compose.material3.Text(
                text = "Welcome, ${user.firstName} ${user.lastName}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.material3.Text(
                text = user.email,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(32.dp))

        androidx.compose.material3.Button(
            onClick = onLogout,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = com.example.sayurbox.ui.theme.SayurboxGreen
            )
        ) {
            androidx.compose.material3.Text("Logout")
        }
    }
}

// Navigation utilities
object SayurboxNavigation {
    const val WELCOME = "welcome"
    const val SIGNUP = "signup"
    const val SIGNIN = "signin"
    const val MAIN = "main"
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    const val ALL_PRODUCTS = "all_products"
    const val PROFILE = "profile"

    fun productDetail(productId: String) = "product_detail/$productId"
}

// App-level state management
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): SayurboxAppState {
    return remember(navController) {
        SayurboxAppState(navController)
    }
}

class SayurboxAppState(
    val navController: NavHostController
) {
    fun navigateToProductDetail(productId: String) {
        navController.navigate(SayurboxNavigation.productDetail(productId))
    }

    fun navigateToAllProducts() {
        navController.navigate(SayurboxNavigation.ALL_PRODUCTS)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToMain() {
        navController.navigate(SayurboxNavigation.MAIN) {
            popUpTo(SayurboxNavigation.WELCOME) { inclusive = true }
            launchSingleTop = true
        }
    }
}