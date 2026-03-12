package com.example.kmp_training.lession4.staggeredgallery.data

import androidx.compose.ui.graphics.Color

data class Photo(
    val id: Int,
    val title: String,
    val category: String,
    val color: Color,
)

val categories = listOf("All", "Nature", "City", "People", "Food", "Travel")

val samplePhotos = listOf(
    Photo(1, "Mountain Sunrise", "Nature", Color(0xFF4CAF50)),
    Photo(2, "City at Night", "City", Color(0xFF3F51B5)),
    Photo(3, "Portrait", "People", Color(0xFFE91E63)),
    Photo(4, "Street Food", "Food", Color(0xFFFF9800)),
    Photo(5, "Beach Sunset", "Travel", Color(0xFFFF5722)),
    Photo(6, "Forest Path", "Nature", Color(0xFF8BC34A)),
    Photo(7, "Skyscrapers", "City", Color(0xFF607D8B)),
    Photo(8, "Coffee Art", "Food", Color(0xFF795548)),
    Photo(9, "Desert Dunes", "Travel", Color(0xFFFFEB3B)),
    Photo(10, "Waterfall", "Nature", Color(0xFF00BCD4)),
    Photo(11, "Night Market", "City", Color(0xFF9C27B0)),
    Photo(12, "Family Photo", "People", Color(0xFFFF4081)),
    Photo(13, "Sushi", "Food", Color(0xFFF06292)),
    Photo(14, "Eiffel Tower", "Travel", Color(0xFF5C6BC0)),
    Photo(15, "Cherry Blossoms", "Nature", Color(0xFFEC407A)),
    Photo(16, "Subway Station", "City", Color(0xFF42A5F5)),
    Photo(17, "Graduation", "People", Color(0xFF26A69A)),
    Photo(18, "Pizza", "Food", Color(0xFFEF5350)),
    Photo(19, "Bali Temple", "Travel", Color(0xFFAB47BC)),
    Photo(20, "Ocean Waves", "Nature", Color(0xFF29B6F6)),
    Photo(21, "Bridge View", "City", Color(0xFF66BB6A)),
    Photo(22, "Street Performer", "People", Color(0xFFFF7043)),
    Photo(23, "Ramen Bowl", "Food", Color(0xFFDCE775)),
    Photo(24, "Tokyo Streets", "Travel", Color(0xFF26C6DA)),
)
