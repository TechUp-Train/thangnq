package com.apero.composetraining.session5.exercises

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
 * ⭐⭐⭐⭐ BÀI TẬP 4: Auth + Tab App (Advanced — 120 phút)
 *
 * Pattern này xuất hiện trong HẦU HẾT production apps.
 *
 * Yêu cầu:
 * - Auth flow: LoginKey → RegisterKey → ForgotPasswordKey
 * - Main flow: 3 tabs (Feed, Discover, Profile) với per-tab stacks
 * - `isAuthenticated: Boolean` state ở App level
 * - Login success → isAuthenticated = true → hiện Main flow (Auth stack bị unmount)
 * - Logout → isAuthenticated = false → hiện Auth flow
 * - Back từ Main KHÔNG về Login sau khi đã login
 * - BackHandler trên ForgotPasswordKey: "Bạn có chắc muốn quay lại?"
 * - LaunchedEffect khi vào ProfileKey: giả lập fetch user data (delay + update state)
 *
 * Cấu trúc gợi ý:
 * ```kotlin
 * // Auth keys — sealed class + @Serializable + NavKey
 * @Serializable
 * sealed class AuthKey : NavKey {
 *     @Serializable data object Login : AuthKey()
 *     @Serializable data object Register : AuthKey()
 *     @Serializable data object ForgotPassword : AuthKey()
 * }
 *
 * // Main keys
 * @Serializable data object FeedKey : NavKey
 * @Serializable data class PostDetailKey(val postId: Int) : NavKey
 * @Serializable data object DiscoverKey : NavKey
 * @Serializable data object ProfileKey : NavKey
 * @Serializable data object EditProfileKey : NavKey
 *
 * // App level — conditional rendering
 * @Composable
 * fun AuthTabApp() {
 *     var isAuthenticated by remember { mutableStateOf(false) }
 *
 *     if (isAuthenticated) {
 *         MainFlow(onLogout = { isAuthenticated = false })
 *     } else {
 *         AuthFlow(onLoginSuccess = { isAuthenticated = true })
 *     }
 * }
 *
 * // Auth flow — back stack riêng, bị destroy khi switch sang Main
 * @Composable
 * fun AuthFlow(onLoginSuccess: () -> Unit) {
 *     val backStack = rememberNavBackStack<Any>(AuthKey.Login)
 *     NavDisplay(
 *         backStack = backStack,
 *         onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
 *         entryProvider = entryProvider {
 *             entry<AuthKey.Login> {
 *                 LoginScreen(
 *                     onLogin = onLoginSuccess,
 *                     onRegister = { backStack.add(AuthKey.Register) },
 *                     onForgotPassword = { backStack.add(AuthKey.ForgotPassword) }
 *                 )
 *             }
 *             entry<AuthKey.Register> { RegisterScreen(onBack = { backStack.removeLastOrNull() }) }
 *             entry<AuthKey.ForgotPassword> {
 *                 var showDialog by remember { mutableStateOf(false) }
 *                 BackHandler { showDialog = true }
 *                 ForgotPasswordScreen(...)
 *                 if (showDialog) ConfirmBackDialog(...)
 *             }
 *         }
 *     )
 * }
 *
 * // Main flow — 3 tabs, per-tab stacks
 * @Composable
 * fun MainFlow(onLogout: () -> Unit) {
 *     val feedStack = rememberNavBackStack<Any>(FeedKey)
 *     val discoverStack = rememberNavBackStack<Any>(DiscoverKey)
 *     val profileStack = rememberNavBackStack<Any>(ProfileKey)
 *     var currentTab by rememberSaveable { mutableStateOf(Tab.FEED) }
 *     val currentStack = when(currentTab) { ... }
 *
 *     Scaffold(bottomBar = { ... }) { padding ->
 *         NavDisplay(
 *             backStack = currentStack,
 *             onBack = { ... },
 *             entryProvider = entryProvider {
 *                 entry<FeedKey> { FeedScreen(...) }
 *                 entry<PostDetailKey> { key -> PostDetailScreen(postId = key.postId) }
 *                 entry<ProfileKey> {
 *                     // LaunchedEffect: fetch user data khi vào screen
 *                     var user by remember { mutableStateOf<UserData?>(null) }
 *                     LaunchedEffect(Unit) {
 *                         delay(500) // giả lập network
 *                         user = UserData("Nguyễn Quang Minh", "Android Dev")
 *                     }
 *                     ProfileScreen(user = user, onLogout = onLogout)
 *                 }
 *                 // ...
 *             }
 *         )
 *     }
 * }
 * ```
 *
 * Tiêu chí nghiệm thu:
 * - Login → Main: back button KHÔNG về Login
 * - Switch tab → back stack của tab cũ được giữ nguyên
 * - BackHandler trên ForgotPassword hiện dialog xác nhận
 * - LaunchedEffect: ProfileScreen hiện loading → data sau delay
 * - Logout → Auth flow (không thể back về Main)
 */

// TODO: [Session 6] Bài tập 4 - Định nghĩa AuthKey sealed class
// @Serializable
// sealed class AuthKey : NavKey { ... }

// TODO: [Session 6] Bài tập 4 - Định nghĩa Main keys
// @Serializable data object FeedKey : NavKey
// @Serializable data class PostDetailKey(val postId: Int) : NavKey
// ...

// TODO: [Session 6] Bài tập 4 - data class UserData(val name: String, val role: String)

// TODO: [Session 6] Bài tập 4 - enum class Tab { FEED, DISCOVER, PROFILE }

// TODO: [Session 6] Bài tập 4 - Implement AuthTabApp (top-level: isAuthenticated state)
@Composable
fun AuthTabApp() {
    // TODO: isAuthenticated state
    // if (isAuthenticated) MainFlow(...) else AuthFlow(...)

    // Placeholder
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("TODO: Implement Auth + Tab App")
    }
}

// TODO: [Session 6] Bài tập 4 - AuthFlow composable
// Params: onLoginSuccess: () -> Unit

// TODO: [Session 6] Bài tập 4 - MainFlow composable
// Params: onLogout: () -> Unit

// TODO: [Session 6] Bài tập 4 - Implement tất cả screen composables
// LoginScreen, RegisterScreen, ForgotPasswordScreen
// FeedScreen, PostDetailScreen, DiscoverScreen, ProfileScreen, EditProfileScreen

@Preview(showBackground = true)
@Composable
private fun AuthTabAppPreview() {
    AppTheme { AuthTabApp() }
}
