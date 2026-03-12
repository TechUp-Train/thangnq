package com.example.kmp_training.lesson6

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmp_training.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: Animated Like Button (Easy)
 *
 * Yêu cầu:
 * - Icon heart: click toggle filled/outlined
 * - animateColorAsState: Gray → Red
 * - animateFloatAsState: scale 1.0 → 1.3 → 1.0 (graphicsLayer)
 * - Dùng Spring animation, KHÔNG tween
 */

@Composable
fun AnimatedLikeButton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Animated Like Button", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        var isLiked by remember { mutableStateOf(false) }

        val color by animateColorAsState(
            targetValue = if (isLiked) Color.Red else Color.Gray,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )

        val scale by animateFloatAsState(
            targetValue = if (isLiked) 1.3f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )

        Icon(
            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
                .clip(CircleShape)
                .clickable { isLiked = !isLiked }
                .padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedLikeButtonPreview() {
    AppTheme { AnimatedLikeButton() }
}
