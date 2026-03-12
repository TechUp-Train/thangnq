package com.example.kmp_training.lession5.auth_exercise

import androidx.navigation3.runtime.NavKey
import com.example.kmp_training.lession5.auth_exercise.data.Post
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthKey : NavKey {
    @Serializable
    data object Login : AuthKey()

    @Serializable
    data object Register : AuthKey()

    @Serializable
    data object ForgotPassword : AuthKey()
}

@Serializable
data object FeedKey : NavKey

@Serializable
data class PostDetailKey(val post: Post) : NavKey

@Serializable
data object DiscoverKey : NavKey

@Serializable
data object ProfileKey : NavKey

@Serializable
data object EditProfileKey : NavKey
