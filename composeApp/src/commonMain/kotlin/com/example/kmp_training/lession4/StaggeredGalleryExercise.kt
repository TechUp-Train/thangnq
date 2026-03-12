package com.example.kmp_training.lession4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmp_training.lession4.staggeredgallery.components.CategoryFilterRow
import com.example.kmp_training.lession4.staggeredgallery.components.StaggeredPhotoGrid
import com.example.kmp_training.lession4.staggeredgallery.data.categories
import com.example.kmp_training.lession4.staggeredgallery.data.samplePhotos
import kotlinx.coroutines.delay
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.lession4.staggeredgallery.components.PhotoCard

/**
 * ⭐⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 4: Pinterest-style Staggered Gallery
 *
 * Mô tả: Gallery với chiều cao item khác nhau (staggered) + category filtering
 *
 * ┌─────────────────────────────────────────────────────┐
 * │ 📸 Gallery                                       🔍 │  ← TopAppBar
 * ├─────────────────────────────────────────────────────┤
 * │ [All] [Nature] [City] [People] [Food] [Travel]      │  ← FilterChip LazyRow
 * ├──────────────────────┬──────────────────────────────┤
 * │ ┌──────────────────┐ │ ┌──────────────────────────┐ │
 * │ │  Short photo     │ │ │   Tall photo              │ │  ← Staggered grid
 * │ │  (Nature)        │ │ │   (City)                  │ │
 * │ └──────────────────┘ │ │                           │ │
 * │ ┌──────────────────┐ │ │                           │ │
 * │ │  Very tall photo │ │ └──────────────────────────┘ │
 * │ │  (People)        │ │ ┌──────────────────────────┐ │
 * │ │                  │ │ │  Short photo (Food)       │ │
 * │ └──────────────────┘ │ └──────────────────────────┘ │
 * └──────────────────────┴──────────────────────────────┘
 *
 * Key concepts:
 * - LazyVerticalStaggeredGrid: render grid với các item chiều cao khác nhau
 * - StaggeredGridCells.Adaptive(150.dp): số cột tự động theo width (responsive)
 * - StaggeredGridCells.Fixed(2): luôn 2 cột (không responsive)
 * - PullToRefreshBox: Material3 pull-to-refresh (experimental)
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaggeredGalleryScreen(modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf("All") }
    var isRefreshing by remember { mutableStateOf(false) }

    val filteredPhotos by remember(selectedCategory) {
        derivedStateOf {
            if (selectedCategory == "All") samplePhotos
            else samplePhotos.filter { it.category == selectedCategory }
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1500L)
            isRefreshing = false
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Gallery") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { isRefreshing = true }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CategoryFilterRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "${filteredPhotos.size} photos",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { isRefreshing = true },
                modifier = Modifier.fillMaxSize()
            ) {
                if (filteredPhotos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No photos found in this category.")
                    }
                } else {
                    StaggeredPhotoGrid(
                        photos = filteredPhotos,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800)
@Composable
private fun StaggeredGalleryPreview() {
    AppTheme {
        StaggeredGalleryScreen()
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun StaggeredGalleryDarkPreview() {
    AppTheme {
        StaggeredGalleryScreen()
    }
}

@Preview(showBackground = true, name = "Photo Card Preview")
@Composable
private fun PhotoCardPreview() {
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
            PhotoCard(photo = samplePhotos[0], height = 180.dp, modifier = Modifier.weight(1f))
            PhotoCard(photo = samplePhotos[1], height = 240.dp, modifier = Modifier.weight(1f))
        }
    }
}
