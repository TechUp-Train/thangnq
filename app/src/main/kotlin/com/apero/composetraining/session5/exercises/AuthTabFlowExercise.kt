package com.apero.composetraining.session5.exercises

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.session5.exercises.auth_exercise.auth_flow.AuthFlow
import com.apero.composetraining.session5.exercises.auth_exercise.main_flow.MainFlow

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


/**
 * AuthTabApp - Top-level composable quản lý authentication state
 * 
 * Luồng hoạt động:
 * 1. isAuthenticated = false → Hiển thị AuthFlow (Login/Register/ForgotPassword)
 * 2. User login thành công → isAuthenticated = true → Hiển thị MainFlow (Feed/Discover/Profile)
 * 3. User logout → isAuthenticated = false → Quay về AuthFlow
 * 
 * Đặc điểm quan trọng:
 * - Auth stack và Main stack hoàn toàn độc lập
 * - Khi switch giữa Auth và Main, stack cũ bị destroy
 * - Không thể back từ Main về Auth sau khi đã login
 * - Không thể back từ Auth về Main sau khi đã logout
 */
@Composable
fun AuthTabApp() {
    var isAuthenticated by remember { mutableStateOf(false) }

    if (isAuthenticated) {
        MainFlow(
            onLogout = { isAuthenticated = false }
        )
    } else {
        AuthFlow(
            onLoginSuccess = { isAuthenticated = true }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthTabAppPreview() {
    AppTheme { AuthTabApp() }
}
