package com.example.kmp_training.lession5

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
 * ⭐⭐ BÀI TẬP 2: Tab App với per-tab back stacks (Medium — 60 phút)
 *
 * Key insight: Navigation 3 = back stack là List → mỗi tab có List riêng!
 *
 * Yêu cầu:
 * - 3 tabs: Home / Explore / Profile
 * - Mỗi tab có back stack RIÊNG (3 rememberNavBackStack)
 * - Scaffold + NavigationBar ở dưới
 * - Home tab: có thể navigate vào ArticleDetailKey(articleId)
 * - Explore tab: có thể navigate vào SearchResultKey(query)
 * - Profile tab: có thể navigate vào EditProfileKey
 * - Switch tab: back stack của tab cũ được GIỮ NGUYÊN (không reset)
 * - Back ở root của tab: không crash (backStack.size == 1 → không pop)
 *
 * Gợi ý cấu trúc:
 * ```kotlin
 * // Keys — cần @Serializable + NavKey
 * @Serializable data object HomeTabKey : NavKey
 * @Serializable data class ArticleDetailKey(val articleId: Int) : NavKey
 * @Serializable data object ExploreTabKey : NavKey
 * @Serializable data class SearchResultKey(val query: String) : NavKey
 * @Serializable data object ProfileTabKey : NavKey
 * @Serializable data object EditProfileKey : NavKey
 *
 * enum class Tab { HOME, EXPLORE, PROFILE }
 *
 * // 3 back stacks riêng biệt — QUAN TRỌNG
 * val homeStack = rememberNavBackStack(HomeTabKey)
 * val exploreStack = rememberNavBackStack(ExploreTabKey)
 * val profileStack = rememberNavBackStack(ProfileTabKey)
 *
 * var selectedTab by rememberSaveable { mutableStateOf(Tab.HOME) }
 * val currentStack = when (selectedTab) {
 *     Tab.HOME -> homeStack
 *     Tab.EXPLORE -> exploreStack
 *     Tab.PROFILE -> profileStack
 * }
 *
 * Scaffold(bottomBar = { TabBar(selectedTab, onTabSelect = { selectedTab = it }) }) { padding ->
 *     NavDisplay(
 *         backStack = currentStack,
 *         onBack = { if (currentStack.size > 1) currentStack.removeLastOrNull() },
 *         entryProvider = entryProvider {
 *             entry<HomeTabKey> { HomeTabScreen(onArticleClick = { homeStack.add(ArticleDetailKey(it)) }) }
 *             entry<ArticleDetailKey> { key -> ArticleDetailScreen(articleId = key.articleId) }
 *             // ... entries cho các tab khác
 *         }
 *     )
 * }
 * ```
 *
 * Tiêu chí nghiệm thu:
 * - Switch tab giữ back stack của tab cũ
 * - Back ở tab root không crash
 * - Navigate vào detail rồi switch tab, quay lại tab → vẫn thấy detail
 */

// @Serializable data object HomeTabKey : NavKey
// @Serializable data class ArticleDetailKey(val articleId: Int) : NavKey
// ... thêm keys cho Explore và Profile tabs

@Serializable
data object HomeScreenKey : NavKey

@Serializable
data class ArticleDetailKey(val id: Int) : NavKey

@Serializable
data object ExploreScreenKey : NavKey

@Serializable
data class SearchResultKey(val query: String) : NavKey

@Serializable
data object ProfileScreenKey : NavKey

@Serializable
data object EditProfileKey : NavKey

enum class Tab { HOME, EXPLORE, PROFILE }

@Composable
fun TabAppScreen() {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(HomeScreenKey::class)
                subclass(ExploreScreenKey::class)
                subclass(ProfileScreenKey::class)
                subclass(ArticleDetailKey::class)
                subclass(SearchResultKey::class)
                subclass(EditProfileKey::class)
            }
        }
    }
    val homeBackStack = rememberNavBackStack(config, HomeScreenKey)
    val exploreBackStack = rememberNavBackStack(config, ExploreScreenKey)
    val profileBackStack = rememberNavBackStack(config, ProfileScreenKey)

    var selectedTab by rememberSaveable { mutableStateOf(Tab.HOME) }
    val currentBackStack = when (selectedTab) {
        Tab.HOME -> homeBackStack
        Tab.EXPLORE -> exploreBackStack
        Tab.PROFILE -> profileBackStack
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            TabBar(
                selectedTab = selectedTab,
                onTabSelect = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavDisplay(
                backStack = currentBackStack,
                onBack = {
                    if (currentBackStack.size > 1) {
                        currentBackStack.removeLastOrNull()
                    }
                },
                entryProvider = entryProvider {
                    entry<HomeScreenKey> {
                        HomeTabScreen(
                            onArticleClick = { articleId ->
                                currentBackStack.add(ArticleDetailKey(articleId))
                            }
                        )
                    }

                    entry<ArticleDetailKey> { key ->
                        ArticleDetailScreen(
                            id = key.id,
                            onBack = {
                                if (homeBackStack.size > 1) {
                                    currentBackStack.removeLastOrNull()
                                }
                            }
                        )
                    }

                    entry<ExploreScreenKey> {
                        ExploreTabScreen(
                            onDirectToSearchScreen = { query ->
                                currentBackStack.add(SearchResultKey(query))
                            }
                        )
                    }

                    entry<SearchResultKey> { key ->
                        SearchResultScreen(
                            query = key.query,
                            onBack = {
                                if (exploreBackStack.size > 1) {
                                    currentBackStack.removeLastOrNull()
                                }
                            }
                        )
                    }

                    entry<ProfileScreenKey> {
                        ProfileTabScreen(
                            onDirectToEditProfile = {
                                currentBackStack.add(EditProfileKey)
                            }
                        )
                    }

                    entry<EditProfileKey> {
                        EditProfileScreen(
                            onBack = {
                                if (profileBackStack.size > 1) {
                                    currentBackStack.removeLastOrNull()
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun TabBar(
    selectedTab: Tab,
    onTabSelect: (Tab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = selectedTab == Tab.HOME,
            onClick = { onTabSelect(Tab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTab == Tab.EXPLORE,
            onClick = { onTabSelect(Tab.EXPLORE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Explore"
                )
            },
            label = { Text("Explore") }
        )
        NavigationBarItem(
            selected = selectedTab == Tab.PROFILE,
            onClick = { onTabSelect(Tab.PROFILE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") }
        )
    }
}

@Composable
fun HomeTabScreen(onArticleClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Home Tab",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Latest Articles",
            style = MaterialTheme.typography.titleMedium,
        )

        repeat(5) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onArticleClick(index + 1) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Article ${index + 1}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Click to read more about this article...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleDetailScreen(id: Int, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable(
                        onClick = onBack
                    )
            )
            Text(text = "Article $id", style = MaterialTheme.typography.headlineMedium)
        }
        Text(
            text = "Article $id Details",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "This is the detailed content of article $id. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ExploreTabScreen(
    onDirectToSearchScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Explore Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            placeholder = { Text("Vui lòng nhập từ khóa cần tìm kiếm") }
        )
        Button(
            onClick = {
                if (searchQuery.isNotBlank()) {
                    onDirectToSearchScreen(searchQuery)
                }
            },
            enabled = searchQuery.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val popularTopics = listOf("Technology", "Science", "Travel", "Food", "Sports")
            popularTopics.forEach { topic ->
                SuggestionChip(
                    onClick = { onDirectToSearchScreen(topic) },
                    label = { Text(topic) }
                )
            }
        }
    }
}

@Composable
fun SearchResultScreen(query: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable(
                        onClick = onBack
                    )
            )
            Text(text = "Search $query", style = MaterialTheme.typography.headlineMedium)
        }
        Text(
            text = "Search Results for \"$query\"",
            style = MaterialTheme.typography.titleLarge
        )
    }

}

@Composable
fun ProfileTabScreen(onDirectToEditProfile: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile Tab",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(8.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Name",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "john.doe@example.com",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider()

                ProfileInfoRow("Username", "")
                ProfileInfoRow("Location", "")
                ProfileInfoRow("Member Since", "")
            }
        }

        Button(
            onClick = onDirectToEditProfile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile")
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun EditProfileScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable(
                            onClick = onBack
                        )
                )
                Text(text = "Edit Profile", style = MaterialTheme.typography.headlineMedium)
            }
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            }
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            }
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TabAppScreenPreview() {
    AppTheme { TabAppScreen() }
}

@Preview
@Composable
private fun HomeTabScreenPreview() {
    HomeTabScreen(onArticleClick = {})
}

@Preview
@Composable
private fun ArticleDetailScreenPreview() {
    ArticleDetailScreen(id = 1, onBack = {})
}

@Preview
@Composable
private fun ExploreTabScreenPreview() {
    ExploreTabScreen(
        onDirectToSearchScreen = {},
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
}

@Preview
@Composable
private fun SearchResultScreenPreview() {
    SearchResultScreen(query = "Technology", onBack = {})
}

@Preview
@Composable
private fun ProfilePreview() {
    ProfileTabScreen(
        onDirectToEditProfile = {},
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
}

@Preview
@Composable
private fun EditProfilePreview() {
    EditProfileScreen(onBack = {})
}
