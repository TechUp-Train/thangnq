package com.example.kmp_training.lesson6

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.common.SampleData
import com.example.kmp_training.lesson6.components.FaqItemCard

/**
 * ⭐⭐ BÀI TẬP 2: Expandable FAQ List (Medium)
 *
 * Yêu cầu:
 * - LazyColumn: 5 câu hỏi FAQ
 * - Click question → AnimatedVisibility (answer expand/collapse)
 * - Arrow icon rotate 0° → 180° (animateFloatAsState)
 * - animateContentSize trên answer text
 * - Chỉ 1 item expand tại 1 thời điểm
 */

@Composable
fun ExpandableListScreen() {
    val faqItems = SampleData.faqItems

    var expandedItemId by remember { mutableIntStateOf(-1) }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {
            Text("FAQ", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn() {
                items(faqItems, key = { it.id }) { faq ->
                    FaqItemCard(
                        question = faq.question,
                        answer = faq.answer,
                        isExpanded = expandedItemId == faq.id,
                        onToggle = { expandedItemId = if (expandedItemId == faq.id) -1 else faq.id }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandableListScreenPreview() {
    AppTheme { ExpandableListScreen() }
}
