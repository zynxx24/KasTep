package com.kastep.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepGray
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2500L)
        onNavigateToLogin()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Wallet Icon drawn with Canvas
            Canvas(modifier = Modifier.size(80.dp)) {
                val cyan = KastepCyan
                val strokeWidth = 3.dp.toPx()

                // Wallet body
                drawRoundRect(
                    color = cyan,
                    topLeft = Offset(size.width * 0.1f, size.height * 0.3f),
                    size = Size(size.width * 0.7f, size.height * 0.55f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )

                // Card flap sticking out top
                drawRoundRect(
                    color = cyan,
                    topLeft = Offset(size.width * 0.2f, size.height * 0.12f),
                    size = Size(size.width * 0.55f, size.height * 0.35f),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                    style = Stroke(width = strokeWidth * 0.8f)
                )

                // Clasp circle on right side
                val claspCenterX = size.width * 0.72f
                val claspCenterY = size.height * 0.57f
                drawCircle(
                    color = cyan,
                    radius = 8.dp.toPx(),
                    center = Offset(claspCenterX, claspCenterY),
                    style = Stroke(width = strokeWidth)
                )
                drawCircle(
                    color = cyan,
                    radius = 3.dp.toPx(),
                    center = Offset(claspCenterX, claspCenterY)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KASTEP text
            Text(
                text = "KASTEP",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Animated progress line
            Canvas(
                modifier = Modifier
                    .width(160.dp)
                    .height(3.dp)
            ) {
                // Background line
                drawLine(
                    color = Color(0xFF1A237E),
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = size.height,
                    cap = StrokeCap.Round
                )
                // Animated progress
                val lineWidth = size.width * 0.4f
                val startX = (size.width + lineWidth) * progress - lineWidth
                drawLine(
                    color = KastepCyan,
                    start = Offset(startX.coerceAtLeast(0f), size.height / 2),
                    end = Offset((startX + lineWidth).coerceAtMost(size.width), size.height / 2),
                    strokeWidth = size.height,
                    cap = StrokeCap.Round
                )
                // Dots at ends
                drawCircle(
                    color = KastepCyan,
                    radius = 4.dp.toPx(),
                    center = Offset(0f, size.height / 2)
                )
                drawCircle(
                    color = KastepCyan,
                    radius = 4.dp.toPx(),
                    center = Offset(size.width, size.height / 2)
                )
            }
        }

        // Loading text at bottom
        Text(
            text = "Loading...",
            color = KastepGray,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.BottomCenter)
                .then(
                    Modifier.background(Color.Transparent)
                ),
        )

        // Bottom padding for loading text
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Loading...",
                color = KastepGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .then(Modifier.height(60.dp))
            )
        }
    }
}
