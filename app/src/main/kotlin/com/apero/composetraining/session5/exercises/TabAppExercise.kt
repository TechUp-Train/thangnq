package com.apero.composetraining.session5.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐ BÀI TẬP 2: Tab App (Medium)
 *
 * Yêu cầu:
 * - 3-tab app: Home / Search / Profile
 * - Scaffold + NavigationBar (BottomNavigation)
 * - Mỗi tab là 1 screen riêng
 * - Home: LazyColumn (items list)
 * - Search: TextField + filtered list
 * - Profile: Static profile card
 * - saveState = true (giữ scroll position khi switch tab)
 * - launchSingleTop = true
 * - Selected tab highlight
 */

@Composable
fun TabAppScreen() {
    // TODO: [Session 5] Bài tập 2 - Tạo NavController
    // val navController = rememberNavController()

    // TODO: [Session 5] Bài tập 2 - Scaffold với NavigationBar
    // Scaffold(
    //     bottomBar = {
    //         NavigationBar {
    //             NavigationBarItem(
    //                 icon = { Icon(Icons.Default.Home, "Home") },
    //                 label = { Text("Home") },
    //                 selected = ...,
    //                 onClick = {
    //                     navController.navigate(Home) {
    //                         popUpTo(navController.graph.startDestinationId) { saveState = true }
    //                         launchSingleTop = true
    //                         restoreState = true
    //                     }
    //                 }
    //             )
    //             // ... Search, Profile tabs
    //         }
    //     }
    // )

    // TODO: [Session 5] Bài tập 2 - NavHost với 3 routes: Home, Search, Profile

    // Placeholder
    Text("Bắt đầu code Tab App ở đây!", modifier = Modifier.padding(16.dp))
}

@Preview(showBackground = true)
@Composable
private fun TabAppScreenPreview() {
    AppTheme { TabAppScreen() }
}
