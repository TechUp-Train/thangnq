package com.example.kmp_training.lession5.auth_exercise.data

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val author: String
)