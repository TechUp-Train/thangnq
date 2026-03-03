package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

/**
 * ⭐⭐⭐ BÀI TẬP 3: Photo Gallery (Challenge)
 *
 * Yêu cầu:
 * - LazyRow tabs: "All", "Nature", "City", "People"
 * - LazyVerticalGrid (hoặc StaggeredGrid) hiển thị photos
 * - Mỗi photo card: placeholder + title overlay (Box)
 * - Grid: Adaptive(150.dp)
 * - Photos có chiều cao khác nhau để thấy staggered effect
 * - Bonus: Scaffold + FAB "Add Photo"
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen() {
    val photos = SampleData.photos
    val categories = listOf("All", "Nature", "City", "People")

    // TODO: [Session 2] Bài tập 3 - Dùng remember để lưu selected category
    // var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        topBar = {
            // TODO: [Session 2] Bài tập 3 - TopAppBar "📸 Photo Gallery"
        },
        floatingActionButton = {
            // TODO: [Session 2] Bài tập 3 - FAB với icon "+" (Add Photo)
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // === CATEGORY TABS ===
            // TODO: [Session 2] Bài tập 3 - LazyRow hiển thị category tabs
            // Mỗi tab: FilterChip hoặc Button
            // Click → đổi selectedCategory → filter photos

            // === PHOTO GRID ===
            // TODO: [Session 2] Bài tập 3 - LazyVerticalGrid hiển thị photos đã filter
            // columns = GridCells.Adaptive(150.dp)
            // Mỗi photo: Box chứa placeholder (height = photo.heightDp.dp) + Text overlay
            // Gợi ý: filteredPhotos = if (selectedCategory == "All") photos else photos.filter { it.category == selectedCategory }

            // Placeholder — xóa khi bắt đầu làm
            Text("Bắt đầu code Photo Gallery ở đây!", modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoGalleryScreenPreview() {
    AppTheme { PhotoGalleryScreen() }
}
