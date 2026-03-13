package com.example.kmp_training.data

import com.example.kmp_training.common.DataResult
import com.example.kmp_training.data.model.NewsFeed
import kmptraining.composeapp.generated.resources.Res
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class NewsFeedRepositoryImpl : NewsFeedRepository {
    override fun getNewsFeeds(): Flow<DataResult<List<NewsFeed>>> = flow {
        emit(DataResult.Loading)
        try {
            delay(1500)
            val bytes = Res.readBytes("files/news_feed.json")
            val jsonString = bytes.decodeToString()
            emit(DataResult.Success(Json.decodeFromString(jsonString)))
        } catch (e: Exception) {
            emit(DataResult.Error(e))
        }
    }
}