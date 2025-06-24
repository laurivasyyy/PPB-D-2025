// MainActivity.kt
package com.example.sayurbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.sayurbox.navigation.SayurboxApp
import com.example.sayurbox.ui.theme.SayurboxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SayurboxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SayurboxApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}