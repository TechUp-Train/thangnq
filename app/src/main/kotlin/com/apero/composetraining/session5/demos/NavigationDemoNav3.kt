package com.apero.composetraining.session5.demos

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.apero.composetraining.common.AppTheme
import kotlinx.serialization.Serializable

// ============================================================
// DEMO NAV 3: So sánh với Nav 2.x trong NavigationDemo.kt
//
// THAY ĐỔI SO VỚI NAV 2.X:
// - NavController (black box)  → rememberNavBackStack() (transparent List)
// - navigate("route/string")   → backStack.add(TypedKey)
// - popBackStack()             → backStack.removeLastOrNull()
// - string routes (typo risk)  → @Serializable data class/object (type-safe)
// - NavHost + composable {}    → NavDisplay + entryProvider { entry<T> {} }
// ============================================================

// ─── Navigation Keys (type-safe, @Serializable + NavKey) ─────
@Serializable
data object WelcomeKey : NavKey

@Serializable
data object HomeKey : NavKey

@Serializable
data class ArticleDetailKey(val articleId: Int) : NavKey

// ─── Demo 1: 2 Screens — Basic Nav3 ──────────────────────────

@Composable
fun BasicNav3Demo() {
    // Back stack = rememberNavBackStack (DSL version)
    val backStack = rememberNavBackStack(WelcomeKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<WelcomeKey> {
                WelcomeNav3Screen(
                    onGetStarted = dropUnlessResumed {
                        backStack.add(HomeKey)
                    }
                )
            }
            entry<HomeKey> {
                HomeNav3Screen(
                    onOpenArticle = { id ->
                        backStack.add(ArticleDetailKey(articleId = id))
                    },
                    onLogout = {
                        backStack.clear()
                        backStack.add(WelcomeKey)
                    }
                )
            }
            entry<ArticleDetailKey> { key ->
                ArticleDetailNav3Screen(
                    articleId = key.articleId,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}

// ─── Demo 2: Tab App với per-tab back stacks ──────────────────

@Serializable
data object FeedKey : NavKey

@Serializable
data object ExploreKey : NavKey

@Serializable
data class PostDetailKey(val postId: Int) : NavKey

@Composable
fun TabNav3Demo() {
    // Mỗi tab có back stack RIÊNG
    val feedStack = rememberNavBackStack(FeedKey)
    val exploreStack = rememberNavBackStack(ExploreKey)

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val activeStack = when (selectedTab) {
        0 -> feedStack
        else -> exploreStack
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = dropUnlessResumed {
                        if (selectedTab == 0) {
                            while (feedStack.size > 1) feedStack.removeLastOrNull()
                        } else selectedTab = 0
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Feed") },
                    label = { Text("Feed") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = dropUnlessResumed { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
                    label = { Text("Explore") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            NavDisplay(
                backStack = activeStack,
                onBack = {
                    if (activeStack.size > 1) activeStack.removeLastOrNull()
                    else if (selectedTab != 0) selectedTab = 0
                },
                entryProvider = entryProvider {
                    entry<FeedKey> {
                        FeedNav3Screen(
                            onPostClick = { postId ->
                                feedStack.add(PostDetailKey(postId = postId))
                            }
                        )
                    }
                    entry<PostDetailKey> { key ->
                        PostDetailNav3Screen(
                            postId = key.postId,
                            onBack = { feedStack.removeLastOrNull() }
                        )
                    }
                    entry<ExploreKey> {
                        ExploreNav3Screen()
                    }
                }
            )
        }
    }
}

// ─── Demo 3: Auth Flow ────────────────────────────────────────

@Serializable
sealed class AuthNavKey : NavKey {
    @Serializable
    data object Login : AuthNavKey()

    @Serializable
    data object Register : AuthNavKey()
}

@Composable
fun AuthFlowNav3Demo() {
    var isAuthenticated by remember { mutableStateOf(false) }

    if (isAuthenticated) {
        MainNav3(onLogout = { isAuthenticated = false })
    } else {
        AuthNav3(onLoginSuccess = { isAuthenticated = true })
    }
}

@Composable
private fun AuthNav3(onLoginSuccess: () -> Unit) {
    val authStack = rememberNavBackStack(AuthNavKey.Login)

    NavDisplay(
        backStack = authStack,
        onBack = { if (authStack.size > 1) authStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<AuthNavKey.Login> {
                LoginNav3Screen(
                    onLogin = onLoginSuccess,
                    onRegister = dropUnlessResumed { authStack.add(AuthNavKey.Register) }
                )
            }
            entry<AuthNavKey.Register> {
                RegisterNav3Screen(
                    onBack = { authStack.removeLastOrNull() },
                    onSuccess = onLoginSuccess
                )
            }
        }
    )
}

@Composable
private fun MainNav3(onLogout: () -> Unit) {
    val mainStack = rememberNavBackStack(HomeKey)

    NavDisplay(
        backStack = mainStack,
        onBack = { mainStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<HomeKey> {
                HomeWithLogoutScreen(
                    onLogout = {
                        mainStack.clear()
                        onLogout()
                    }
                )
            }
        }
    )
}

// ─── Screen Composables (Demo UI) ────────────────────────────

@Composable
private fun WelcomeNav3Screen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👋 Welcome!", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Nav 3 Demo", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onGetStarted) { Text("Bắt đầu →") }
    }
}

@Composable
private fun HomeNav3Screen(onOpenArticle: (Int) -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("🏠 Home", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            "backStack = List<NavKey> — inspect freely!",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
        Spacer(Modifier.height(8.dp))
        (1..3).forEach { id ->
            OutlinedButton(
                onClick = { onOpenArticle(id) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("📄 Article $id") }
        }
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Đăng xuất")
        }
    }
}

@Composable
private fun ArticleDetailNav3Screen(articleId: Int, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📄 Article #$articleId", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            "Params qua type-safe key!",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("← Back") }
    }
}

@Composable
private fun FeedNav3Screen(onPostClick: (Int) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("📰 Feed Tab", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        (1..5).forEach { id ->
            Card(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { onPostClick(id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Post #$id — tap to navigate")
                }
            }
        }
    }
}

@Composable
private fun PostDetailNav3Screen(postId: Int, onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Post #$postId", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) { Text("← Back") }
    }
}

@Composable
private fun ExploreNav3Screen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("🔍 Explore Tab\nSwitch về Feed → back stack vẫn giữ!")
    }
}

@Composable
private fun LoginNav3Screen(onLogin: () -> Unit, onRegister: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🔐 Login", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onLogin, modifier = Modifier.fillMaxWidth()) { Text("Đăng nhập") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRegister) { Text("Chưa có tài khoản? Đăng ký") }
    }
}

@Composable
private fun RegisterNav3Screen(onBack: () -> Unit, onSuccess: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📝 Register", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onSuccess, modifier = Modifier.fillMaxWidth()) { Text("Tạo tài khoản") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onBack) { Text("← Quay lại") }
    }
}

@Composable
private fun HomeWithLogoutScreen(onLogout: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("✅ Đã đăng nhập!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Back button KHÔNG về Login — Auth stack bị destroy.", fontSize = 13.sp)
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = { showConfirm = true }) { Text("Đăng xuất") }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Đăng xuất?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    onLogout()
                }) { Text("Đăng xuất") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Ở lại") }
            }
        )
    }
}

// ─── Previews ────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun BasicNav3DemoPreview() {
    AppTheme { BasicNav3Demo() }
}

@Preview(showBackground = true)
@Composable
private fun TabNav3DemoPreview() {
    AppTheme { TabNav3Demo() }
}

@Preview(showBackground = true)
@Composable
private fun AuthFlowNav3DemoPreview() {
    AppTheme { AuthFlowNav3Demo() }
}
