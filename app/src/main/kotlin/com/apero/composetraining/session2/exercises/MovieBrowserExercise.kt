package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData
import com.apero.composetraining.common.Movie

/**
 * ⭐⭐ BONUS: Movie Browser (tham khảo NewsFeedExercise)
 *
 * Yêu cầu:
 * - LazyRow trên cùng: "Trending" horizontal scroll (poster 120x180dp)
 * - LazyColumn phía dưới: "All Movies" vertical list
 * - Mỗi movie item: Row(Poster + Column(Title + Year + Rating))
 * - Scaffold với TopAppBar "🎬 Movies"
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieBrowserScreen() {
    val movies = SampleData.movies

    Scaffold(
        topBar = {
            // TODO: [Session 2] Bài tập 2 - Tạo TopAppBar với title "🎬 Movies"
            TopAppBar()
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // === TRENDING SECTION ===
            // TODO: [Session 2] Bài tập 2 - Thêm Text "🔥 Trending" (padding 16dp)
            Text(text = "\uD83D\uDD25 Trending", modifier = Modifier.padding(16.dp))
            // TODO: [Session 2] Bài tập 2 - Tạo LazyRow hiển thị 8 movies đầu tiên
            // Mỗi item: Card 120x180dp với poster placeholder + title
            // Gợi ý: movies.take(8), horizontalArrangement = Arrangement.spacedBy(12.dp)
            val data = movies.take(8)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(data, key = { it.id }) {
                    MovieCardItem(
                        movie = it,
                        modifier = Modifier.size(width = 120.dp, height = 180.dp)
                    )
                }
            }

            // === ALL MOVIES SECTION ===
            // TODO: [Session 2] Bài tập 2 - Thêm Text "📋 All Movies" (padding 16dp)
            Text(text = "\uD83D\uDCCB All Movie", modifier = Modifier.padding(16.dp))
            // TODO: [Session 2] Bài tập 2 - Tạo LazyColumn hiển thị tất cả movies
            // Mỗi item: Row chứa poster placeholder (80x120dp) + Column(title, year, rating)
            // Gợi ý: items(movies, key = { it.id })
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies, key = { movie -> movie.id }) {
                    MovieCardItem(
                        movie = it,
                        modifier = Modifier.size(width = 80.dp, height = 120.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    FlowRow(modifier = Modifier.padding(16.dp)) {
        Icon(imageVector = Icons.Default.Movie, contentDescription = null)
        Text(
            text = "Movies",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun MovieCardItem(movie: Movie, modifier: Modifier = Modifier) {
    Row() {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 18.sp
            )

            Text(
                text = "${movie.year} ",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 18.sp
            )
            Text(
                text = "${movie.rating} ",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 18.sp
            )
        }
    }
}

@Preview
@Composable
private fun MovieItemPreview() {
    val movie = Movie(
        id = 1,
        title = "The Shawshank Redemption",
        year = 1994,
        rating = 9.3f,
        genre = "Drama",
        posterUrl = "https://picsum.photos/seed/movie1/200/300"
    )

    MovieCardItem(movie = movie)
}

@Preview(showBackground = true)
@Composable
private fun MovieBrowserScreenPreview() {
    AppTheme { MovieBrowserScreen() }
}
