package com.apero.composetraining.session4.exercises

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.apero.composetraining.session4.exercises.designsystem.components.ProductCard
import com.apero.composetraining.session4.exercises.designsystem.theme.AppDesignThemeWrapper

/**
 * ⭐⭐⭐ BÀI TẬP 3: Design System (Challenge)
 *
 * Yêu cầu:
 * - data class AppColors (success, warning, error, aiPrimary, trending, gradient)
 * - CompositionLocalProvider
 * - AppDesignTheme wrapper
 * - Product Card với:
 *   - "AI RECOMMENDED" badge (aiPrimary color)
 *   - "TRENDING" pill (trending color)
 *   - Price text (custom typography)
 * - AppDesignTheme.colors.xxx syntax hoạt động
 */

@Composable
fun DesignSystemProductCard() {
    AppDesignThemeWrapper {
        ProductCard()
    }
}

@Preview(showBackground = true)
@Composable
private fun DesignSystemProductCardPreview() {
    DesignSystemProductCard()
}
