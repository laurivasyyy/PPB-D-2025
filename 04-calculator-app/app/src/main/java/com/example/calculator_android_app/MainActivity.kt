package com.example.calculator_android_app

import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator_android_app.ui.theme.CalculatorAndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var num1 by remember { mutableStateOf("0") }
            var num2 by remember { mutableStateOf("0") }

            Column {
                TextField(
                    value = num1,
                    onValueChange = {
                        num1 = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = num2,
                    onValueChange = {
                        num2 = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Button(onClick = {
                        var result = num1.toInt() + num2.toInt()
                        Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                        Text("Add")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        var result = num1.toInt() - num2.toInt()
                        Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                        Text("Sub")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        var result = num1.toInt() * num2.toInt()
                        Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                        Text("Multiply")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        var result = num1.toInt() / num2.toInt()
                        Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                        Text("Divide")
                    }
                }
            }
        }
    }
}
