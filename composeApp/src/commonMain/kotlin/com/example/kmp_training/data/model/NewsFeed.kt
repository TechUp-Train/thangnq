package com.example.kmp_training.data.model

import kotlinx.serialization.Serializable


@Serializable
data class NewsFeed(
    val id: Int,
    val title: String,
    val content: String,
    val author: String? = null,
    var isLiked: Boolean,
    val createdAt: Long,
    val imageUrl: String = "https://picsum.photos/seed/movie$id/200/300",
)