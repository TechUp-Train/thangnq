package com.example.kmp_training.lesson6

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.common.SampleData
import com.example.kmp_training.lesson6.components.SwipeableProfileCard
import kotlin.math.abs

/**
 * ⭐⭐⭐ BÀI TẬP 3: Tinder Swipe Card (Challenge)
 *
 * Yêu cầu:
 * - 5 profile cards stacked (Box)
 * - Drag gesture: detectDragGestures
 * - Swipe left = "Nope" (red overlay), right = "Like" (green overlay)
 * - Card rotation theo drag offset
 * - animateFloatAsState cho snap back khi thả giữa
 * - Card tiếp theo slide lên khi top card bị swipe
 * - Drag amount > threshold → dismiss
 * - Drag amount < threshold → snap back với spring
 */

@Composable
fun SwipeCardScreen() {
    val profiles = SampleData.profileCards
    var currentIndex by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var enableButton by remember { mutableStateOf(true) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = if (isDragging) snap() else spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "offsetX",
        finishedListener = { finalValue ->
            if (abs(finalValue) == 1000f) {
                currentIndex++
                isDragging = true
                offsetX = 0f
            }
        }
    )

    LaunchedEffect(offsetX) {
        if (offsetX == 0f && isDragging) {
            isDragging = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Swipe Cards", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val visibleProfiles = profiles.drop(currentIndex).take(3)
            visibleProfiles.reversed().forEachIndexed { index, profile ->
                val isTopCard = index == visibleProfiles.size - 1
                SwipeableProfileCard(
                    profile = profile,
                    isTopCard = isTopCard,
                    offsetX = if (isTopCard) animatedOffsetX else 0f,
                    modifier = Modifier
                        .then(
                            if (isTopCard) {
                                Modifier.pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { isDragging = true },
                                        onDragEnd = {
                                            isDragging = false
                                            offsetX = if (abs(offsetX) > 600f) {
                                                if (offsetX > 0) 1000f else -1000f
                                            } else {
                                                0f
                                            }
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            offsetX += dragAmount.x
                                        }
                                    )
                                }
                            } else Modifier
                        )
                )
            }
            if (visibleProfiles.isEmpty()) {
                enableButton = false
                Text("No more profiles!")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = {
                    isDragging = false
                    offsetX = -1000f
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red,
                    disabledContentColor = Color.Gray
                ),
                border = BorderStroke(
                    1.dp,
                    color = if (enableButton) Color.Red else Color.Gray
                ),
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                enabled = profiles.drop(currentIndex).isNotEmpty()
            ) {
                Text("X", fontSize = 40.sp)
            }

            OutlinedButton(
                onClick = {
                    isDragging = false
                    offsetX = 1000f
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Green,
                    disabledContentColor = Color.Gray
                ),
                border = BorderStroke(
                    1.dp,
                    if (enableButton) Color.Green else Color.Gray
                ),
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                enabled = profiles.drop(currentIndex).isNotEmpty()
            ) {
                Icon(imageVector = Icons.Filled.WbSunny, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeCardScreenPreview() {
    AppTheme { SwipeCardScreen() }
}
