package com.apero.composetraining.session4.exercises.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.lession4.designsystem.theme.AppColors
import com.example.kmp_training.lession4.designsystem.theme.LocalAppColors

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
