package com.example.kmp_training.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmp_training.common.DataResult
import com.example.kmp_training.common.UiState
import com.example.kmp_training.data.model.NewsFeed
import com.example.kmp_training.domain.GetNewsFeedUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsFeedViewModel(private val userCase: GetNewsFeedUseCase) : ViewModel() {
    private val _state = MutableStateFlow<NewsFeedUiState>(NewsFeedUiState())
    val state: StateFlow<NewsFeedUiState> = _state

    private var currentNews: List<NewsFeed> = emptyList()

    fun processIntent(intent: NewsFeedIntent) {
        when (intent) {
            is NewsFeedIntent.LoadNews -> loadNews()
            is NewsFeedIntent.LoadDetailNews -> loadDetailNews(intent.newsId)
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            userCase.getNewsFeed().collectLatest { result ->
                when (result) {
                    is DataResult.Loading -> {
                        _state.update {
                            it.copy(newsState = UiState.Loading)
                        }
                    }

                    is DataResult.Success -> {
                        _state.update {
                            it.copy(newsState = UiState.Success(result.data))
                        }
                        currentNews = result.data
                    }

                    is DataResult.Error -> {
                        _state.update {
                            it.copy(
                                newsState = UiState.Error(
                                    message = result.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadDetailNews(newId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(detailNewsState = UiState.Loading) }
            delay(1500)
            val item = currentNews.find { it.id == newId }
            if (item != null) {
                _state.update { it.copy(detailNewsState = UiState.Success(item)) }
            } else {
                _state.update { it.copy(detailNewsState = UiState.Error(message = "News not found")) }
            }
        }
    }
}

data class NewsFeedUiState(
    val newsState: UiState<List<NewsFeed>> = UiState.Loading,
    val detailNewsState: UiState<NewsFeed> = UiState.Loading
)

sealed class NewsFeedIntent {
    data object LoadNews : NewsFeedIntent()
    data class LoadDetailNews(val newsId: Int) : NewsFeedIntent()
}