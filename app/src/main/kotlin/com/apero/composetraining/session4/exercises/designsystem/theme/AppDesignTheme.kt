package com.apero.composetraining.session4.exercises.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.apero.composetraining.common.AppTheme

object AppDesignTheme {
    val colors: AppColors @Composable get() = LocalAppColors.current
}

@Composable
fun AppDesignThemeWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppColors provides AppColors()) {
        AppTheme {
            content()
        }
    }
}
