package com.apero.composetraining.session5.exercises.auth_exercise.main_flow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.session5.exercises.auth_exercise.data.Post

@Composable
fun FeedScreen(
    onPostClick: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    val posts = listOf(
        Post(
            1,
            "Getting Started with Jetpack Compose",
            "Learn the basics of Compose...",
            "John Doe"
        ),
        Post(
            2,
            "State Management in Compose",
            "Understanding state and recomposition...",
            "Jane Smith"
        ),
        Post(3, "Navigation in Compose", "Building multi-screen apps...", "Bob Johnson"),
        Post(4, "Material Design 3", "Implementing Material You...", "Alice Brown"),
        Post(5, "Performance Tips", "Optimizing your Compose app...", "Charlie Wilson")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onPostClick(post) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = post.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "By ${post.author}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    FeedScreen(onPostClick = {})
}