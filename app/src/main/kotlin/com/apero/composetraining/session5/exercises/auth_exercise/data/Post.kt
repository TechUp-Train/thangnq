package com.apero.composetraining.session5.exercises.auth_exercise.data

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val author: String
)