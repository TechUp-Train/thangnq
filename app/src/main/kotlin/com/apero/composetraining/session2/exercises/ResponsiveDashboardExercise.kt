package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP 3: Responsive Dashboard (Khó — 60 phút)
 *
 * Yêu cầu:
 * - BoxWithConstraints để detect screen width
 *   → Phone (maxWidth < 600.dp): LazyColumn, 1 card per row
 *   → Tablet (maxWidth ≥ 600.dp): LazyVerticalGrid, 2 columns
 * - Stats row: 4 stats cột ngang với VerticalDivider, dùng IntrinsicSize.Min để bằng cao
 * - Premium banner với Modifier.drawBehind (custom gradient background)
 *
 * Tiêu chí:
 * - BoxWithConstraints đúng cách (dùng maxWidth từ constraints)
 * - Modifier.height(IntrinsicSize.Min) + VerticalDivider trên stats row
 * - Modifier.drawBehind { drawRect(brush = gradient) } cho banner
 * - KHÔNG hardcode layout cho device type
 *
 * Gợi ý BoxWithConstraints:
 * BoxWithConstraints {
 *     if (maxWidth < 600.dp) {
 *         PhoneLayout(stats, items, isPremium)
 *     } else {
 *         TabletLayout(stats, items, isPremium)
 *     }
 * }
 *
 * Gợi ý drawBehind:
 * Modifier.drawBehind {
 *     drawRect(
 *         brush = Brush.horizontalGradient(listOf(Color(0xFF6750A4), Color(0xFF9C27B0)))
 *     )
 * }
 */

data class StatItem(
    val label: String,
    val value: String,
    val unit: String = ""
)

data class DashboardItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String
)

private val sampleStats = listOf(
    StatItem("Apps", "19", "apps"),
    StatItem("Downloads", "2.4M", ""),
    StatItem("Rating", "4.8", "⭐"),
    StatItem("Reviews", "12K", "")
)

private val sampleItems = (1..8).map { i ->
    DashboardItem(
        i,
        "App #$i",
        "Mô tả app $i ngắn gọn",
        listOf("AI", "Photo", "Video", "Utility")[i % 4]
    )
}

@Composable
fun DashboardScreen(
    stats: List<StatItem> = sampleStats,
    items: List<DashboardItem> = sampleItems,
    isPremium: Boolean = false
) {
    BoxWithConstraints {
        if (maxWidth < 600.dp) {
            PhoneLayout(stats, items, isPremium)
        } else {
            TabletLayout(stats, items, isPremium)
        }
    }
}

@Composable
fun StatsRow(stats: List<StatItem>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        stats.forEachIndexed { index, stat ->
            StatColumn(stat, Modifier.weight(1f))
            if (index < stats.size - 1) {
                VerticalDivider()
            }
        }
    }
}

@Composable
fun StatColumn(stat: StatItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stat.value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stat.label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (stat.unit.isNotEmpty()) {
            Text(
                text = stat.unit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PremiumBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .drawBehind(onDraw = {
                drawRect(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.4f)
                        )

                    )
                )
            })
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = "⭐ Premium Member",
                style = MaterialTheme.typography.titleMedium,
                color = androidx.compose.ui.graphics.Color.White
            )
            Text(
                text = "Enjoy unlimited access to all features",
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun PhoneLayout(
    stats: List<StatItem>,
    items: List<DashboardItem>,
    isPremium: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isPremium) {
            item {
                PremiumBanner()
            }
        }

        item {
            Card {
                StatsRow(stats)
            }
        }

        items(items.size) { index ->
            DashboardCard(items[index])
        }
    }
}

@Composable
fun TabletLayout(
    stats: List<StatItem>,
    items: List<DashboardItem>,
    isPremium: Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isPremium) {
            item(span = { GridItemSpan(2) }) {
                PremiumBanner()
            }
        }

        item(span = { GridItemSpan(2) }) {
            Card {
                StatsRow(stats)
            }
        }

        items(items) {
            DashboardCard(it)
        }
    }
}

@Composable
fun DashboardCard(item: DashboardItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            SuggestionChip(
                onClick = { },
                label = { Text(item.category) }
            )
        }
    }
}

@Composable
fun ResponsiveDashboardScreen() {
    DashboardScreen(isPremium = true)
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PhonePreview() {
    AppTheme { ResponsiveDashboardScreen() }
}

@Preview(showBackground = true, widthDp = 720)
@Composable
private fun TabletPreview() {
    AppTheme { ResponsiveDashboardScreen() }
}
