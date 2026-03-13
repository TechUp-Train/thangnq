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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmp_training.common.UiState
import com.example.kmp_training.screen.components.NewsFeedList
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.koinInject

@Composable
fun NewsFeedScreen() {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(NewsFeedKey.NewsFeeds::class)
                subclass(NewsFeedKey.DetailNews::class)
            }
        }
    }
    val backStack = rememberNavBackStack(config, NewsFeedKey.NewsFeeds)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<NewsFeedKey.NewsFeeds> {
                ListNewsScreen(onClickItem = { newsId ->
                    backStack.add(NewsFeedKey.DetailNews(newsId))
                })
            }
            entry<NewsFeedKey.DetailNews> {
                DetailNewsScreen(id = it.newsId, onBack = { backStack.removeLastOrNull() })
            }
        },
    )
}

@Composable
fun ListNewsScreen(
    onClickItem: (newsId: Int) -> Unit = {},
) {
    val viewModel: NewsFeedViewModel = koinInject()
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
                        news = newsState.data,
                        onClickItem = onClickItem
                    )
                }
            }
        }
    }
}