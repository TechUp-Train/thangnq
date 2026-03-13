package com.example.kmp_training.data

import com.example.kmp_training.common.DataResult
import com.example.kmp_training.data.model.NewsFeed
import kotlinx.coroutines.flow.Flow

interface NewsFeedRepository {
    fun getNewsFeeds(): Flow<DataResult<List<NewsFeed>>>
}