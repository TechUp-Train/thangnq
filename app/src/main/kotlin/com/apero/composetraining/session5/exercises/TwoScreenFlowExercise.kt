package com.apero.composetraining.session5.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: 2-Screen Flow (Easy)
 *
 * Yêu cầu:
 * - WelcomeScreen: Text + Button "Get Started"
 * - HomeScreen: Text "Welcome back!" + Button "Logout" (popBackStack)
 * - NavHost với 2 composable routes
 * - Back button từ Home → Welcome
 * - Logout clear stack
 */

@Composable
fun TwoScreenFlowApp() {
    // TODO: [Session 5] Bài tập 1 - Tạo NavController
    // val navController = rememberNavController()

    // TODO: [Session 5] Bài tập 1 - Tạo NavHost với 2 routes
    // NavHost(navController, startDestination = ...) {
    //     composable<Welcome> { WelcomeScreen(onGetStarted = { navController.navigate(Home) }) }
    //     composable<Home> { HomeScreen(onLogout = { navController.popBackStack(Welcome, inclusive = false) }) }
    // }

    // Placeholder
    Text("Bắt đầu code 2-Screen Flow ở đây!", modifier = Modifier.padding(16.dp))
}

// TODO: [Session 5] Bài tập 1 - Tạo WelcomeScreen composable
// Params: onGetStarted: () -> Unit
// UI: Column centered, Text "👋 Welcome!", Button "Get Started"

// TODO: [Session 5] Bài tập 1 - Tạo HomeScreen composable
// Params: onLogout: () -> Unit
// UI: Column centered, Text "🏠 Welcome back!", OutlinedButton "Logout"

@Preview(showBackground = true)
@Composable
private fun TwoScreenFlowAppPreview() {
    AppTheme { TwoScreenFlowApp() }
}
