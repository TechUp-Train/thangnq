package com.example.kmp_training.screen

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class NewsFeedKey : NavKey {

    @Serializable
    data object NewsFeeds : NewsFeedKey()

    @Serializable
    data class DetailNews(val newsId: Int) : NewsFeedKey()


}