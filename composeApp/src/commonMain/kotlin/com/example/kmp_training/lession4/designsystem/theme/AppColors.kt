package com.example.kmp_training.lession4.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val success: Color = Color(0xFF4CAF50),
    val warning: Color = Color(0xFFFFC107),
    val error: Color = Color(0xFFF44336),
    val aiPrimary: Color = Color(0xFF7C4DFF),
    val trending: Color = Color(0xFFFF6D00),
    val gradientStart: Color = Color(0xFF1A237E),
    val gradientEnd: Color = Color(0xFF7C4DFF),
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }
