package com.example.kmp_training

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kmp_training.screen.NewsFeedScreen
import com.example.kmp_training.screen.NewsFeedViewModel
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: NewsFeedViewModel = koinInject()
        NewsFeedScreen(viewModel)
    }
}