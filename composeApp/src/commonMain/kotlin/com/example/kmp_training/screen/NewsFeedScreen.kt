package com.example.kmp_training.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kmp_training.common.UiState
import com.example.kmp_training.screen.components.NewsFeedList

@Composable
fun NewsFeedScreen(
    viewModel: NewsFeedViewModel
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.processIntent(
            NewsFeedIntent.LoadNews
        )
    }

    Scaffold { padding ->
        when (val newsState = state.newsState) {
            UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(newsState.message)
                }
            }

            is UiState.Success -> {
                Box(modifier = Modifier.padding(padding)) {
                    NewsFeedList(
                        news = newsState.data
                    )
                }
            }
        }
    }
}