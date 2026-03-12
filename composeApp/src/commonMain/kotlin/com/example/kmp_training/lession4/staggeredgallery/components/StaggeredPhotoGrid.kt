package com.example.kmp_training.lession4.staggeredgallery.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmp_training.lession4.staggeredgallery.data.Photo
import com.example.kmp_training.lession4.staggeredgallery.extension.Extensions.calculateDeterministicHeight

@Composable
fun StaggeredPhotoGrid(
    photos: List<Photo>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(photos, key = { it.id }) { photo ->
            PhotoCard(
                photo = photo,
                height = photo.title.calculateDeterministicHeight()
            )
        }
    }
}

@Preview
@Composable
private fun StaggeredPhotoGridPreview() {
    StaggeredPhotoGrid(
        photos = listOf(
            Photo(
                id = 1,
                title = "Mountain Sunrise",
                category = "Nature",
                color = Color(0xFF4CAF50)
            ),
            Photo(
                id = 2,
                title = "Mountain Sunrise",
                category = "Nature",
                color = Color(0xFF4CAF50)
            ),
        )
    )
}