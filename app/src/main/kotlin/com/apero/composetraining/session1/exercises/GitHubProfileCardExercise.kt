package com.apero.composetraining.session1.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐⭐ BONUS (Dành cho Nguyễn Quang Minh)
 *
 * GitHub Profile Card — Advanced Modifier & Composition
 *
 * Yêu cầu:
 * 1. Avatar với GRADIENT BORDER (Brush.linearGradient)
 * 2. Stats row: Repos / Stars / Followers — Divider giữa các stat
 * 3. Language chips: FlowRow layout (dùng FlowRow từ accompanist HOẶC
 *    tự implement wrap bằng Layout composable)
 * 4. Pinned repos: 2 cards EQUAL HEIGHT (IntrinsicSize.Max)
 *    + graphicsLayer scale effect khi "pressed" (mock với alpha)
 * 5. Tất cả component phải có modifier param
 * 6. 3 @Preview: Portrait / Large font / Dark mode
 *
 * Khái niệm nâng cao:
 * - Brush.linearGradient cho gradient border
 * - graphicsLayer { scaleX; scaleY; alpha } cho visual effects
 * - Custom Layout composable (nếu tự implement FlowRow)
 * - IntrinsicSize.Max + fillMaxHeight
 *
 * NOTE: File này là SCAFFOLD — các TODO là phần học viên tự implement
 */

// ─── Data Models ──────────────────────────────────────────────────────────────

data class GitHubProfile(
    val username: String,
    val displayName: String,
    val bio: String,
    val location: String,
    val repoCount: Int,
    val starCount: Int,
    val followerCount: Int,
    val languages: List<String>,
    val pinnedRepos: List<PinnedRepo>
)

data class PinnedRepo(
    val name: String,
    val description: String,
    val language: String,
    val stars: Int
)

// ─── Sample Data ──────────────────────────────────────────────────────────────

val sampleProfile = GitHubProfile(
    username = "nqmgaming",
    displayName = "Nguyễn Quang Minh",
    bio = "Android & Flutter Developer · Open Source enthusiast",
    location = "Hà Nội, Việt Nam 🇻🇳",
    repoCount = 39,
    starCount = 47,
    followerCount = 12,
    languages = listOf("Kotlin", "Dart", "TypeScript", "Python", "Rust", "Go"),
    pinnedRepos = listOf(
        PinnedRepo("shose-shop", "Online shoe shopping app", "Kotlin", 9),
        PinnedRepo("ANeko-Reborn", "Revived pet app with Compose", "Kotlin", 23)
    )
)

// ─── Main Component ──────────────────────────────────────────────────────────

@Composable
fun GitHubProfileCard(
    profile: GitHubProfile,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // 1. Header: Avatar + Name + Bio
            ProfileHeader(
                username = profile.username,
                displayName = profile.displayName,
                bio = profile.bio,
                location = profile.location
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // 2. Stats row
            ProfileStats(
                repoCount = profile.repoCount,
                starCount = profile.starCount,
                followerCount = profile.followerCount
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Languages
            Text(
                text = "Languages",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            LanguageChips(languages = profile.languages)

            if (profile.pinnedRepos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // 4. Pinned repos
                Text(
                    text = "Pinned",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                PinnedReposRow(repos = profile.pinnedRepos)
            }
        }
    }
}

// ─── Sub-components ──────────────────────────────────────────────────────────

@Composable
fun ProfileHeader(
    username: String,
    displayName: String,
    bio: String,
    location: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar với gradient border (Instagram-style)
        GradientAvatar(username = username, size = 72)

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@$username",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = bio,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "📍 $location",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Avatar với GRADIENT BORDER — advanced Modifier trick
 *
 *
 * Cách implement gradient border:
 * Box(modifier = Modifier
 *     .size(size.dp + 4.dp)  // Slightly larger for border
 *     .clip(CircleShape)
 *     .background(
 *         Brush.linearGradient(
 *             colors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
 *         )
 *     )
 * ) {
 *     Box(modifier = Modifier
 *         .size(size.dp)
 *         .clip(CircleShape)
 *         .background(MaterialTheme.colorScheme.surface)
 *         .align(Alignment.Center)
 *     ) {
 *         // Avatar content
 *     }
 * }
 */
@Composable
fun GradientAvatar(
    username: String,
    size: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size((size + 4).dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF833AB4), // Instagram purple
                        Color(0xFFFD1D1D), // Red
                        Color(0xFFFCB045)  // Orange/yellow
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.first().uppercaseChar().toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = (size / 2.5).sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileStats(
    repoCount: Int,
    starCount: Int,
    followerCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            icon = Icons.Outlined.FolderOpen,
            count = repoCount,
            label = "repos"
        )

        VerticalDivider(modifier = Modifier.height(32.dp))

        StatItem(
            icon = Icons.Outlined.Star,
            count = starCount,
            label = "stars"
        )

        VerticalDivider(modifier = Modifier.height(32.dp))

        StatItem(
            icon = Icons.Outlined.People,
            count = followerCount,
            label = "followers"
        )
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Language chips — Wrap layout
 *
 *
 * Option 1 (Easy): Dùng accompanist FlowRow
 * dependencies { implementation "com.google.accompanist:accompanist-flowlayout:0.34.0" }
 *
 * Option 2 (Hard): Tự implement với Layout composable
 * @Composable
 * fun FlowRow(content: @Composable () -> Unit) {
 *     Layout(content = content) { measurables, constraints ->
 *         // Measure và place từng item, wrap khi hết row width
 *     }
 * }
 *
 * Hôm nay: Dùng Row wrap = nếu nhiều lang thì scroll hoặc chia 2 Row
 */
@Composable
fun LanguageChips(
    languages: List<String>,
    modifier: Modifier = Modifier
) {
    val firstRow = languages.take(3)
    val secondRow = languages.drop(3)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            firstRow.forEach { lang -> LanguageChip(language = lang) }
        }
        if (secondRow.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                secondRow.forEach { lang -> LanguageChip(language = lang) }
            }
        }
    }
}

@Composable
fun LanguageChip(
    language: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = language,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Pinned repos — EQUAL HEIGHT với IntrinsicSize.Max
 *
 *
 * Cách implement hover/press effect với graphicsLayer:
 * var isPressed by remember { mutableStateOf(false) }
 * PinnedRepoCard(
 *     modifier = Modifier
 *         .clickable { ... }
 *         .graphicsLayer {
 *             scaleX = if (isPressed) 0.97f else 1f
 *             scaleY = if (isPressed) 0.97f else 1f
 *         }
 * )
 * Note: State cần Buổi 3 — hôm nay mock với alpha thay đổi tĩnh
 */
@Composable
fun PinnedReposRow(
    repos: List<PinnedRepo>,
    modifier: Modifier = Modifier
) {
    // EQUAL HEIGHT — IntrinsicSize.Max
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max), // ← Equal height magic
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repos.forEach { repo ->
            PinnedRepoCard(
                repo = repo,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight() // ← Phối hợp với IntrinsicSize.Max
            )
        }
    }
}

@Composable
fun PinnedRepoCard(
    repo: PinnedRepo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable {}
            .graphicsLayer { },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = repo.language,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Stars",
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = repo.stars.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "GitHub Profile — Portrait")
@Composable
private fun GitHubProfilePortraitPreview() {
    AppTheme {
        GitHubProfileCard(profile = sampleProfile)
    }
}

@Preview(
    showBackground = true,
    name = "GitHub Profile — Large Font",
    fontScale = 1.5f
)
@Composable
private fun GitHubProfileLargeFontPreview() {
    AppTheme {
        GitHubProfileCard(profile = sampleProfile)
    }
}

@Preview(
    showBackground = true,
    name = "GitHub Profile — Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun GitHubProfileDarkPreview() {
    AppTheme {
        GitHubProfileCard(profile = sampleProfile)
    }
}
