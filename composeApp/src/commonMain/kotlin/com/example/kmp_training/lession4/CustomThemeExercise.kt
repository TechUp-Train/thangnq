package com.example.kmp_training.lession4

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmp_training.lession4.custom_theme.screens.DetailScreen
import com.example.kmp_training.lession4.custom_theme.screens.HomeScreen
import com.example.kmp_training.lession4.custom_theme.theme.MyAppTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * ⭐⭐ BÀI TẬP 2: Custom Theme (Medium)
 *
 * Yêu cầu:
 * - Định nghĩa lightColorScheme + darkColorScheme riêng
 * - Custom Typography (có thể dùng system font)
 * - Tạo wrapper MyAppTheme { content }
 * - Áp dụng lên 2 screens: Home + Detail
 * - Consistent color usage (không hardcode hex trong composable)
 * - isSystemInDarkTheme() tự switch
 */

@Serializable
sealed class CustomThemeKey : NavKey {
    @Serializable
    object Home : CustomThemeKey()

    @Serializable
    data class Detail(val itemId: Int) : CustomThemeKey()
}

@Composable
fun CustomThemeApp() {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(CustomThemeKey.Home::class)
                subclass(CustomThemeKey.Detail::class)
            }
        }
    }
    val backStack = rememberNavBackStack(config, CustomThemeKey.Home)

    MyAppTheme {
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<CustomThemeKey.Home> {
                    HomeScreen(
                        onItemClick = { itemId ->
                            backStack.add(CustomThemeKey.Detail(itemId))
                        }
                    )
                }

                entry<CustomThemeKey.Detail> { key ->
                    DetailScreen(
                        itemId = key.itemId,
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        }
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomThemeAppPreview() {
    val dispatcher = remember { NavigationEventDispatcher() }

    val dispatcherOwner = remember {
        object : NavigationEventDispatcherOwner {
            override val navigationEventDispatcher = dispatcher
        }
    }

    CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides dispatcherOwner) {
        CustomThemeApp()
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun DarkCustomThemeAppPreview() {
    val dispatcher = remember { NavigationEventDispatcher() }

    val dispatcherOwner = remember {
        object : NavigationEventDispatcherOwner {
            override val navigationEventDispatcher = dispatcher
        }
    }

    CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides dispatcherOwner) {
        CustomThemeApp()
    }
}