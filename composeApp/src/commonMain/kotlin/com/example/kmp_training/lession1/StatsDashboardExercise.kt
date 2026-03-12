package com.example.kmp_training.lession1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmp_training.common.AppTheme

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO: Stats Dashboard
 *
 * Yêu cầu:
 * 1. StatCard với Slot API cho trend indicator
 * 2. TrendIndicator component (icon + %, màu xanh/đỏ)
 * 3. Dashboard 2x2 grid dùng Row + Column
 * 4. EQUAL HEIGHT cards — bắt buộc dùng IntrinsicSize.Max + fillMaxHeight()
 * 5. Compose Phase optimization — TrendIcon dùng graphicsLayer thay vì rotate()
 * 6. 3 @Preview: All Positive / Mixed / Dark Mode
 *
 * Khái niệm áp dụng từ Buổi 1:
 * - Slot API (trend: @Composable () -> Unit)
 * - IntrinsicSize.Max — equal height trick (học từ slide 6 Modifier)
 * - graphicsLayer { rotationZ } — skip Layout phase (Slide 11!)
 * - Modifier chain & order
 */

// ─── Data Model ───────────────────────────────────────────────────────────────

data class StatData(
    val label: String,
    val value: String,
    val percentage: String,
    val isPositive: Boolean,
    val emoji: String = "📊"
)

// ─── Components ──────────────────────────────────────────────────────────────

/**
 * Card hiển thị 1 stat với Slot API cho trend
 *
 * → Caller có thể truyền bất kỳ UI nào vào (TrendIndicator, Chart, Badge...)
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier,
    // Slot API cho trend indicator — caller quyết định UI
    trend: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()    // fillMaxHeight() phối hợp với IntrinsicSize.Max
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: emoji + label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Value — số to
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Trend slot
            trend()
        }
    }
}

/**
 * Trend indicator: icon mũi tên + percentage + màu
 *
 *
 * // ❌ Cách 1: Modifier.rotate() — trigger cả 3 phases (Composition + Layout + Drawing)
 * Icon(modifier = Modifier.rotate(if (isPositive) 0f else 180f))
 *
 * // ✅ Cách 2: graphicsLayer — skip Composition + Layout, chỉ Drawing
 * Icon(modifier = Modifier.graphicsLayer { rotationZ = if (isPositive) 0f else 180f })
 *
 * Với static UI cả 2 cho kết quả giống nhau, nhưng trong animation thì graphicsLayer
 * hiệu quả hơn nhiều vì không trigger layout pass.
 */
@Composable
fun TrendIndicator(
    percentage: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isPositive) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.error
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Đây là ví dụ thực tế của Slide 11 — Smart Optimization
        Icon(
            imageVector = if (isPositive) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
            contentDescription = if (isPositive) "Tăng" else "Giảm",
            tint = color,
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer { }
        )

        Text(
            text = percentage,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Dashboard chứa 4 StatCards trong 2x2 grid
 *
 *
 * Vấn đề: 2 card trong Row có content khác nhau → height khác nhau → layout không đều
 *
 * Solution:
 * Row(modifier = Modifier.height(IntrinsicSize.Max)) {
 *     StatCard(modifier = Modifier.weight(1f).fillMaxHeight())
 *     StatCard(modifier = Modifier.weight(1f).fillMaxHeight())
 * }
 *
 * Cơ chế: IntrinsicSize.Max đo height của card cao nhất, rồi set cho tất cả
 */
@Composable
fun StatsDashboard(
    stats: List<StatData>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 1 — 2 stat cards EQUAL HEIGHT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (stats.isNotEmpty()) {
                    StatCard(
                        label = stats[0].label,
                        value = stats[0].value,
                        emoji = stats[0].emoji,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(), // ← fillMaxHeight() phối hợp với IntrinsicSize
                        trend = {
                            TrendIndicator(
                                percentage = stats[0].percentage,
                                isPositive = stats[0].isPositive
                            )
                        }
                    )
                }
                if (stats.size > 1) {
                    StatCard(
                        label = stats[1].label,
                        value = stats[1].value,
                        emoji = stats[1].emoji,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        trend = {
                            TrendIndicator(
                                percentage = stats[1].percentage,
                                isPositive = stats[1].isPositive
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Row 2 — 2 stat cards EQUAL HEIGHT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (stats.size > 2) {
                    StatCard(
                        label = stats[2].label,
                        value = stats[2].value,
                        emoji = stats[2].emoji,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        trend = {
                            TrendIndicator(
                                percentage = stats[2].percentage,
                                isPositive = stats[2].isPositive
                            )
                        }
                    )
                }
                if (stats.size > 3) {
                    StatCard(
                        label = stats[3].label,
                        value = stats[3].value,
                        emoji = stats[3].emoji,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        trend = {
                            TrendIndicator(
                                percentage = stats[3].percentage,
                                isPositive = stats[3].isPositive
                            )
                        }
                    )
                }
            }
        }
    }
}

// ─── Sample Data ──────────────────────────────────────────────────────────────

private val allPositiveStats = listOf(
    StatData("Downloads", "12,450", "↑ +23%", true, "📱"),
    StatData("Rating", "4.7 / 5.0", "↑ +0.2", true, "⭐"),
    StatData("Revenue", "\$2,840", "↑ +12%", true, "💰"),
    StatData("Active Users", "8,920", "↑ +5%", true, "👥")
)

private val mixedStats = listOf(
    StatData("Downloads", "12,450", "↑ +23%", true, "📱"),
    StatData("Rating", "4.6 / 5.0", "↓ -0.1", false, "⭐"),
    StatData("Revenue", "\$2,840", "↑ +12%", true, "💰"),
    StatData("Crash Rate", "0.8%", "↑ +0.3%", false, "💥")
)

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Dashboard — All Positive")
@Composable
private fun DashboardAllPositivePreview() {
    AppTheme {
        StatsDashboard(stats = allPositiveStats)
    }
}

@Preview(showBackground = true, name = "Dashboard — Mixed Trends")
@Composable
private fun DashboardMixedPreview() {
    AppTheme {
        StatsDashboard(stats = mixedStats)
    }
}

@Preview(
    showBackground = true,
    name = "Dashboard — Dark Mode",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun DashboardDarkPreview() {
    AppTheme {
        StatsDashboard(stats = mixedStats)
    }
}

// ─── Câu Hỏi Thảo Luận ───────────────────────────────────────────────────────
/*
 * Sau khi hoàn thành, thảo luận với nhóm:
 *
 * Q1: Tại sao IntrinsicSize.Max lại work? Compose tính height như thế nào?
 *     → Compose đo "intrinsic height" (chiều cao tự nhiên) của mỗi child,
 *       lấy max, rồi constrain tất cả về cùng height đó.
 *
 * Q2: Nếu không dùng IntrinsicSize.Max thì UI trông thế nào?
 *     → Mỗi card height = content của nó → 2 card không cùng height → xấu.
 *
 * Q3: graphicsLayer vs Modifier.rotate() — khi nào dùng cái nào?
 *     → Animation → graphicsLayer (skip Layout phase, GPU layer riêng)
 *     → Static rotation → cả 2 đều ok, graphicsLayer không có overhead lớn hơn
 *
 * Q4: Slot API ở StatCard có lợi gì so với truyền thẳng TrendIndicator?
 *     → Caller có thể thay bằng Chart, Badge, hay bất kỳ UI nào
 *     → StatCard không coupled với TrendIndicator
 */
