package com.apero.composetraining.session1.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP NÂNG CAO: Social Post Card
 *
 * Yêu cầu:
 * 1. Header: Avatar (CircleShape, màu từ username) + Username + Timestamp
 * 2. Content: Text với maxLines = 3, TextOverflow.Ellipsis
 * 3. Attachment Slot (NULLABLE) — chỉ render nếu != null
 * 4. Action bar: Like + Comment + Retweet + Share (Arrangement.SpaceBetween)
 * 5. Modifier param bắt buộc trên tất cả @Composable
 * 6. 3 @Preview: không attachment / có image / có code block
 *
 * Khái niệm áp dụng từ Buổi 1:
 * - Slot API với NULLABLE content: (@Composable () -> Unit)? = null
 * - Modifier chain & order
 * - Reuse component (ActionItem)
 * - Multiple @Preview
 */

// ─── Main Component ──────────────────────────────────────────────────────────

@Composable
fun SocialPostCard(
    username: String,
    timeAgo: String,
    content: String,
    likeCount: Int = 0,
    commentCount: Int = 0,
    retweetCount: Int = 0,
    modifier: Modifier = Modifier,
    // TODO: [Nâng cao] Khai báo nullable slot cho attachment
    // Gợi ý: attachment: (@Composable () -> Unit)? = null
    attachment: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // 1. Header: Avatar + Username + Time
            PostHeader(username = username, timeAgo = timeAgo)

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Content text — giới hạn 3 dòng
            // TODO: [Nâng cao] Thêm maxLines và overflow
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // 3. Attachment slot — chỉ render khi có
            // TODO: [Nâng cao] Kiểm tra attachment != null trước khi render
            // Gợi ý: if (attachment != null) { Spacer + attachment() }
            attachment?.let {
                Spacer(modifier = Modifier.height(8.dp))
                it()
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(8.dp))

            // 4. Action bar
            PostActionBar(
                likeCount = likeCount,
                commentCount = commentCount,
                retweetCount = retweetCount
            )
        }
    }
}

// ─── Sub-components (cần tự implement) ───────────────────────────────────────

@Composable
fun PostHeader(
    username: String,
    timeAgo: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: Avatar circle với màu từ username
        // Gợi ý: dùng MaterialTheme.colorScheme.primary hoặc tính màu từ username.hashCode()
        UserAvatar(username = username)

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = timeAgo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UserAvatar(
    username: String,
    modifier: Modifier = Modifier
) {
    // TODO: [Nâng cao] Tính màu từ username.hashCode() thay vì hardcode
    // Gợi ý:
    // val avatarColors = listOf(Color(0xFF1DA1F2), Color(0xFFE0245E), ...)
    // val color = avatarColors[abs(username.hashCode()) % avatarColors.size]

    val avatarColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )
    val color = avatarColors[Math.abs(username.hashCode()) % avatarColors.size]

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = username.first().uppercaseChar().toString(),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PostActionBar(
    likeCount: Int,
    commentCount: Int,
    retweetCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // TODO: [Nâng cao] Tạo ActionItem composable reusable thay vì lặp code
        // Gợi ý: ActionItem(icon, count, onClick, modifier)

        ActionItem(
            icon = Icons.Outlined.FavoriteBorder,
            count = likeCount,
            contentDescription = "Like"
        )
        ActionItem(
            icon = Icons.Outlined.ChatBubbleOutline,
            count = commentCount,
            contentDescription = "Comment"
        )
        ActionItem(
            icon = Icons.Outlined.Repeat,
            count = retweetCount,
            contentDescription = "Retweet"
        )
        ActionItem(
            icon = Icons.Outlined.Share,
            count = null,
            contentDescription = "Share"
        )
    }
}

@Composable
fun ActionItem(
    icon: ImageVector,
    count: Int?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (count != null && count > 0) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

// Preview 1: Không có attachment
@Preview(showBackground = true, name = "Post — No Attachment")
@Composable
private fun SocialPostNoAttachmentPreview() {
    AppTheme {
        SocialPostCard(
            username = "nqmgaming",
            timeAgo = "2 min ago",
            content = "Vừa migrate ANeko sang Jetpack Compose xong! " +
                    "Smart recomposition giúp animation smooth hơn hẳn so với View system. " +
                    "Ai đang dùng Compose production chưa?",
            likeCount = 42,
            commentCount = 12,
            retweetCount = 7
        )
    }
}

// Preview 2: Có Image attachment (dùng Box placeholder)
@Preview(showBackground = true, name = "Post — With Image Attachment")
@Composable
private fun SocialPostWithImagePreview() {
    AppTheme {
        SocialPostCard(
            username = "thang44hdai",
            timeAgo = "15 min ago",
            content = "DSA practice session hôm nay — solved Binary Search Tree in O(log n). " +
                    "Cái này sẽ apply được vào Room database query optimization không nhỉ?",
            likeCount = 15,
            commentCount = 3,
            retweetCount = 2,
            // TODO: [Nâng cao] Thêm Image thật từ painterResource hoặc AsyncImage
            attachment = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "📸 Image Attachment",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

// Preview 3: Có Code block attachment
@Preview(showBackground = true, name = "Post — With Code Block")
@Composable
private fun SocialPostWithCodePreview() {
    AppTheme {
        SocialPostCard(
            username = "KhacMinh2305",
            timeAgo = "1 hour ago",
            content = "Mới học được cái Slot API trong Compose — flexible hơn XML include rất nhiều!",
            likeCount = 8,
            commentCount = 5,
            retweetCount = 1,
            attachment = {
                // Code block attachment — mono font, nền tối
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFF1E1E1E)
                ) {
                    Text(
                        text = """
@Composable
fun AppCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card { 
        content() // Slot!
    }
}""".trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = Color(0xFFD4D4D4)
                    )
                }
            }
        )
    }
}

// Preview 4: Dark Mode
@Preview(
    showBackground = true,
    name = "Post — Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SocialPostDarkPreview() {
    AppTheme {
        SocialPostNoAttachmentPreview()
    }
}
