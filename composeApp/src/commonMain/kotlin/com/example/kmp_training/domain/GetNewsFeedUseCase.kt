package com.example.kmp_training.domain

import com.example.kmp_training.common.DataResult
import com.example.kmp_training.data.NewsFeedRepository
import com.example.kmp_training.data.model.NewsFeed
import kotlinx.coroutines.flow.Flow

class GetNewsFeedUseCase(
    private val repository: NewsFeedRepository
) {
    fun getNewsFeed(): Flow<DataResult<List<NewsFeed>>> {
        return repository.getNewsFeeds()
    }
}