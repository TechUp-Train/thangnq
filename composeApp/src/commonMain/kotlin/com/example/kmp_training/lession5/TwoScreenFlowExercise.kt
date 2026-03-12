package com.example.kmp_training.lession5

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmp_training.common.AppTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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

@Serializable
data object WelcomeKey : NavKey

@Serializable
data object HomeKey : NavKey

@Composable
fun TwoScreenFlowApp() {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(WelcomeKey::class)
                subclass(HomeKey::class)
            }
        }
    }
    val backstack = rememberNavBackStack(config, WelcomeKey)

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<WelcomeKey> {
                WelcomeScreen(
                    onGetStarted = {
                        backstack.add(HomeKey)
                    }
                )
            }
            entry<HomeKey> {
                HomeScreen(
                    onLogout = {
                        backstack.clear()
                        backstack.add(WelcomeKey)
                    }
                )
            }
        }
    )
}

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Chào mừng đến với Compose!", modifier = Modifier.padding(16.dp))
        Button(onClick = onGetStarted) {
            Text(text = "Bắt đầu")
        }
    }
}

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Trang chủ", modifier = Modifier.padding(16.dp))
        Button(onClick = onLogout) {
            Text(text = "Đăng xuất")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TwoScreenFlowPreview() {
    AppTheme { TwoScreenFlowApp() }
}
