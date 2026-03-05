package com.apero.composetraining.session5.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
// TODO: Thêm imports Nav3
// import androidx.navigation3.runtime.NavKey
// import androidx.navigation3.runtime.entryProvider
// import androidx.navigation3.runtime.rememberNavBackStack
// import androidx.navigation3.ui.NavDisplay
// import kotlinx.serialization.Serializable

/**
 * ⭐ BÀI TẬP 1: 2-Screen Flow (Easy — 30 phút)
 *
 * Học Navigation 3: Back stack là List<NavKey>, không phải NavController
 *
 * Yêu cầu:
 * - Định nghĩa 2 keys: @Serializable data object WelcomeKey : NavKey
 * - Back stack: rememberNavBackStack(WelcomeKey)
 * - NavDisplay với entryProvider mapping key → screen
 * - Welcome screen: Button "Bắt đầu" → backStack.add(HomeKey)
 * - Home screen: Button "Đăng xuất" → backStack.clear(); backStack.add(WelcomeKey)
 * - Back button (hardware/gesture) từ Home → Welcome hoạt động đúng
 *
 * Gợi ý:
 * ```kotlin
 * // 1. Keys — cần @Serializable và implement NavKey
 * @Serializable
 * data object WelcomeKey : NavKey
 *
 * @Serializable
 * data object HomeKey : NavKey
 *
 * // 2. Back stack — dùng rememberNavBackStack
 * val backStack = rememberNavBackStack(WelcomeKey)
 *
 * // 3. NavDisplay với entryProvider DSL
 * NavDisplay(
 *     backStack = backStack,
 *     onBack = { backStack.removeLastOrNull() },
 *     entryProvider = entryProvider {
 *         entry<WelcomeKey> { WelcomeScreen(onGetStarted = { backStack.add(HomeKey) }) }
 *         entry<HomeKey> { HomeScreen(onLogout = { backStack.clear(); backStack.add(WelcomeKey) }) }
 *     }
 * )
 * ```
 *
 * Tiêu chí nghiệm thu:
 * - Type-safe keys với @Serializable + NavKey (không dùng String route)
 * - Navigate và back hoạt động đúng
 * - Logout clear stack (không còn back về Home sau logout)
 */

// TODO: [Session 6] Bài tập 1 - Định nghĩa type-safe keys
// @Serializable
// data object WelcomeKey : NavKey
//
// @Serializable
// data object HomeKey : NavKey

// TODO: [Session 6] Bài tập 1 - Implement TwoScreenFlowApp
@Composable
fun TwoScreenFlowApp() {
    // TODO: Tạo back stack
    // val backStack = rememberNavBackStack(WelcomeKey)

    // TODO: NavDisplay với entryProvider cho 2 screens
    // NavDisplay(
    //     backStack = backStack,
    //     onBack = { backStack.removeLastOrNull() },
    //     entryProvider = entryProvider {
    //         entry<WelcomeKey> { ... }
    //         entry<HomeKey> { ... }
    //     }
    // )

    // Placeholder — xóa khi implement
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("TODO: Implement 2-Screen Flow với Navigation 3")
    }
}

// TODO: [Session 6] Bài tập 1 - WelcomeScreen
// Params: onGetStarted: () -> Unit
// UI: Column centered, Text "👋 Chào mừng đến với Compose!", Button "Bắt đầu"
@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    // TODO
}

// TODO: [Session 6] Bài tập 1 - HomeScreen
// Params: onLogout: () -> Unit
// UI: Column centered, Text "🏠 Trang chủ", OutlinedButton "Đăng xuất"
@Composable
fun HomeScreen(onLogout: () -> Unit) {
    // TODO
}

@Preview(showBackground = true)
@Composable
private fun TwoScreenFlowPreview() {
    AppTheme { TwoScreenFlowApp() }
}
