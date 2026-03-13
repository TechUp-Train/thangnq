package com.example.kmp_training.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmp_training.data.model.NewsFeed

@Composable
fun NewsFeedList(
    news: List<NewsFeed>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(news) { item ->
            Card(
                modifier = modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(item.content)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = item.author ?: "Unknown",
                        style = MaterialTheme.typography.labelSmall
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun NewsFeedListPreview() {
    MaterialTheme {
        NewsFeedList(
            news = listOf(
                NewsFeed(
                    id = 1,
                    title = "Title 1",
                    content = "Content 1",
                    isLiked = true,
                    createdAt = 1234567890
                ),
            )
        )
    }
}