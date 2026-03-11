package com.apero.composetraining.session4.exercises

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.apero.composetraining.session4.exercises.custom_theme.screens.DetailScreen
import com.apero.composetraining.session4.exercises.custom_theme.screens.HomeScreen
import com.apero.composetraining.session4.exercises.custom_theme.theme.MyAppTheme
import kotlinx.serialization.Serializable

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
    data object Home : CustomThemeKey()

    @Serializable
    data class Detail(val itemId: Int) : CustomThemeKey()
}

@Composable
fun CustomThemeApp() {
    val backStack = rememberNavBackStack(CustomThemeKey.Home)

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
    uiMode = Configuration.UI_MODE_NIGHT_YES
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