// WelcomeScreen.kt
package com.example.sayurbox.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sayurbox.R
import com.example.sayurbox.ui.theme.SayurboxGreen
import com.example.sayurbox.ui.theme.SayurboxTheme

@Composable
fun WelcomeScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Top spacer to push content towards center
            Spacer(modifier = Modifier.weight(1f))

            // Logo section
            LogoSection()

            // Main content spacer
            Spacer(modifier = Modifier.height(120.dp))

            // Welcome text
            WelcomeText()

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            ActionButtons(
                onNavigateToSignUp = onNavigateToSignUp,
                onNavigateToSignIn = onNavigateToSignIn
            )

            // Bottom spacer
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun LogoSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App logo/icon
        Card(
            modifier = Modifier.size(80.dp),
            colors = CardDefaults.cardColors(containerColor = SayurboxGreen.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier.size(40.dp),
                    tint = SayurboxGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App name
        Text(
            text = stringResource(R.string.app_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // App tagline
        Text(
            text = stringResource(R.string.app_tagline),
            fontSize = 16.sp,
            color = SayurboxGreen,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun WelcomeText() {
    Text(
        text = stringResource(R.string.welcome_title),
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        color = SayurboxGreen,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun ActionButtons(
    onNavigateToSignUp: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sign Up button (Primary)
        Button(
            onClick = onNavigateToSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SayurboxGreen,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            Text(
                text = stringResource(R.string.btn_sign_up),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Sign In button (Secondary)
        OutlinedButton(
            onClick = onNavigateToSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = Color.Transparent
            ),
            border = ButtonDefaults.outlinedButtonBorder(
                enabled = true
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = stringResource(R.string.btn_sign_in),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

// Alternative version with custom illustration
@Composable
fun WelcomeScreenWithIllustration(
    onNavigateToSignUp: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // Illustration area
            IllustrationSection()

            Spacer(modifier = Modifier.height(48.dp))

            // Brand section
            BrandSection()

            Spacer(modifier = Modifier.weight(1f))

            // Welcome message
            WelcomeMessage()

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            ActionButtons(
                onNavigateToSignUp = onNavigateToSignUp,
                onNavigateToSignIn = onNavigateToSignIn
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun IllustrationSection() {
    // Vegetable illustration using emojis
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🥕", fontSize = 40.sp)
        Text("🥦", fontSize = 40.sp)
        Text("🍅", fontSize = 40.sp)
        Text("🍆", fontSize = 40.sp)
        Text("🥬", fontSize = 40.sp)
    }
}

@Composable
private fun BrandSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_title),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.app_tagline),
            fontSize = 18.sp,
            color = SayurboxGreen,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun WelcomeMessage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = SayurboxGreen,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Get fresh vegetables delivered to your doorstep",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SayurboxTheme {
        WelcomeScreen(
            onNavigateToSignUp = {},
            onNavigateToSignIn = {}
        )
    }
}

@Preview(showBackground = true, name = "With Illustration")
@Composable
fun WelcomeScreenWithIllustrationPreview() {
    SayurboxTheme {
        WelcomeScreenWithIllustration(
            onNavigateToSignUp = {},
            onNavigateToSignIn = {}
        )
    }
}