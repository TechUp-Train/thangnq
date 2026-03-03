package com.apero.composetraining.session3.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

/**
 * ⭐⭐⭐ BÀI TẬP 3: Search & Filter (Challenge)
 *
 * Yêu cầu:
 * - TextField search bar (state hoisted)
 * - LazyColumn: filtered contacts (derivedStateOf)
 * - Toggle: "Show favorites only" (Switch)
 * - Empty state: "No contacts found" khi list trống
 * - UDF pattern: data flows down, events flow up
 * - derivedStateOf cho filtered list
 * - rememberSaveable cho search query
 */

@Composable
fun SearchFilterScreen() {
    val contacts = SampleData.contacts

    // TODO: [Session 3] Bài tập 3 - Tạo state cho search query (rememberSaveable)
    // var query by rememberSaveable { mutableStateOf("") }

    // TODO: [Session 3] Bài tập 3 - Tạo state cho toggle favorites
    // var showFavoritesOnly by rememberSaveable { mutableStateOf(false) }

    // TODO: [Session 3] Bài tập 3 - Filter contacts bằng derivedStateOf
    // val filteredContacts by remember {
    //     derivedStateOf {
    //         contacts.filter { contact ->
    //             val matchesQuery = contact.name.contains(query, ignoreCase = true)
    //             val matchesFavorite = if (showFavoritesOnly) contact.isFavorite else true
    //             matchesQuery && matchesFavorite
    //         }
    //     }
    // }

    Column(modifier = Modifier.fillMaxSize()) {
        // TODO: [Session 3] Bài tập 3 - OutlinedTextField cho search
        // modifier = Modifier.fillMaxWidth().padding(16.dp)

        // TODO: [Session 3] Bài tập 3 - Row chứa Text "Chỉ hiện favorites" + Switch

        // TODO: [Session 3] Bài tập 3 - LazyColumn hiển thị filteredContacts
        // Mỗi item: ListItem(headlineContent = name, supportingContent = phone)

        // TODO: [Session 3] Bài tập 3 - Empty state khi filteredContacts rỗng
        // Text "Không tìm thấy liên hệ nào" + emoji

        // Placeholder
        Text("Bắt đầu code Search & Filter ở đây!", modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFilterScreenPreview() {
    AppTheme { SearchFilterScreen() }
}
