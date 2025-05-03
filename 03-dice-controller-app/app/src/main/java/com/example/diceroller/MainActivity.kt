package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1) }
    var rotation by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    var offsetY by remember { mutableStateOf(0.dp) }
    var lastRolled by remember { mutableStateOf(0) }

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "rotation"
    )

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = offsetY,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "offsetY"
    )

    // Reset animation effects after a short delay
    LaunchedEffect(lastRolled) {
        if (lastRolled > 0) {
            delay(150)
            scale = 1f
            offsetY = 0.dp
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "🎲 Dice Roller",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Image(
            painter = painterResource(imageResource),
            contentDescription = result.toString(),
            modifier = Modifier
                .offset(y = animatedOffsetY)
                .graphicsLayer(
                    rotationZ = animatedRotation,
                    scaleX = animatedScale,
                    scaleY = animatedScale
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                result = (1..6).random()
                rotation += 360f
                scale = 1.3f
                offsetY = (-20).dp
                lastRolled++
            }
        ) {
            Text(
                text = if (lastRolled == 0) "Roll" else "Roll Again",
                fontSize = 24.sp
            )
        }
    }
}