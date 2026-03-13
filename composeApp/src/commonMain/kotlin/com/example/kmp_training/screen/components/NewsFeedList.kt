package com.example.kmp_training.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.kmp_training.common.Extensions.formatTimestamp
import com.example.kmp_training.data.model.NewsFeed

@Composable
fun NewsFeedList(
    news: List<NewsFeed>,
    onClickItem: (newsId: Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {

        items(news) { item ->

            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable { onClickItem(item.id) }
            ) {

                Row(
                    modifier = Modifier.height(IntrinsicSize.Min)
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = item.content,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "${item.author} - ${item.createdAt.formatTimestamp()}",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .fillMaxHeight()
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.7f)
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
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
                NewsFeed(
                    id = 2,
                    title = "Title 2",
                    content = "Content 1",
                    isLiked = true,
                    createdAt = 1234567890
                ),
                NewsFeed(
                    id = 3,
                    title = "Title 3",
                    content = "Content 1",
                    isLiked = true,
                    createdAt = 1234567890
                ),
            )
        )
    }
}