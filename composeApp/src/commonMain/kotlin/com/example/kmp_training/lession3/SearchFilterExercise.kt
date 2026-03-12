package com.example.kmp_training.lession3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.common.BgCard
import com.example.kmp_training.common.BgPage
import com.example.kmp_training.common.BorderStrong
import com.example.kmp_training.common.BorderSubtle
import com.example.kmp_training.common.Contact
import com.example.kmp_training.common.PrimaryBlue
import com.example.kmp_training.common.PrimaryGreen
import com.example.kmp_training.common.SampleData
import com.example.kmp_training.common.TextPrimary
import com.example.kmp_training.common.TextSecondary
import com.example.kmp_training.common.TextTertiary
import com.example.kmp_training.common.UncheckTrack
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.ic_empty
import kmptraining.composeapp.generated.resources.outfit_font
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

/**
 * ⭐⭐⭐ BÀI TẬP 3: Search & Filter Contacts (Challenge)
 *
 * Yêu cầu:
 * - TextField search bar (state hoisted lên SearchFilterScreen)
 * - Switch "Chỉ hiện active contacts" (dùng Contact.isFavorite làm active flag)
 * - LazyColumn: danh sách contacts đã filter (dùng derivedStateOf)
 * - Empty state: hiển thị "Không tìm thấy liên hệ nào" khi list trống
 * - rememberSaveable cho search query (survive xoay màn hình)
 * - snapshotFlow để debounce search 300ms (tránh filter mỗi keystroke)
 *
 * Tiêu chí:
 * - derivedStateOf đúng cách cho filter logic
 * - snapshotFlow + debounce(300ms) + distinctUntilChanged đúng cách
 * - UDF pattern: state xuống, events lên
 * - rememberSaveable cho query và toggle
 *
 * Gợi ý snapshotFlow:
 * LaunchedEffect(Unit) {
 *     snapshotFlow { searchQuery }     // chuyển Compose State → Flow
 *         .debounce(300L)              // đợi 300ms user ngừng gõ
 *         .distinctUntilChanged()      // bỏ qua nếu giá trị không đổi
 *         .collect { debouncedQuery = it }
 * }
 */

@OptIn(FlowPreview::class)
@Composable
fun SearchFilterScreen() {
    val contacts = SampleData.contacts

    var searchQuery by rememberSaveable { mutableStateOf("") }

    var debouncedQuery by remember { mutableStateOf("") }

    var showActiveOnly by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }
            .debounce(300L)
            .distinctUntilChanged()
            .collect { debouncedQuery = it }
    }

    val filteredContacts by remember {
        derivedStateOf {
            contacts.filter { contact ->
                val matchesQuery = contact.name.contains(debouncedQuery, ignoreCase = true)
                val matchesActive = if (showActiveOnly) contact.isFavorite else true
                matchesQuery && matchesActive
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        if (filteredContacts.isNotEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chỉ hiện active contacts",
                    fontFamily = FontFamily(Font(Res.font.outfit_font)),
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Switch(
                    checked = showActiveOnly,
                    onCheckedChange = { showActiveOnly = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = PrimaryBlue,
                        checkedBorderColor = Color.Transparent,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = UncheckTrack,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }

        if (filteredContacts.isEmpty()) {
            EmptyComponent()
        } else {
            Surface(
                shape = RoundedCornerShape(16.dp),
//                shadowElevation = 2.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp)),
            ) {
                LazyColumn() {
                    itemsIndexed(filteredContacts) { index, contact ->
                        UserProfile(contact = contact)
                        if (index < filteredContacts.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = BorderSubtle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfile(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    val colorRandoms = listOf<Color>(
        Color(0xFF6B8EAE),
        Color(0xFF9B7EC8),
        Color(0xFFC4956A),
        Color(0xFFD89575),
        Color(0xFF3D8A5A)
    )
    val colorIndex = Random.nextInt(0, 5)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorRandoms[colorIndex],
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(Res.font.outfit_font))
                )
            }

            if (contact.isFavorite) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color.White, CircleShape)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = PrimaryGreen,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(Res.font.outfit_font)),
                fontSize = 16.sp
            )
            Text(
                text = contact.phone,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontFamily = FontFamily(Font(Res.font.outfit_font)),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = {
            Text(
                text = "Tìm kiếm...",
                fontFamily = FontFamily(Font(Res.font.outfit_font)),
                color = TextTertiary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(24.dp)
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderStrong,
            focusedBorderColor = BorderStrong,
            unfocusedContainerColor = BgCard,
            focusedContainerColor = BgCard
        ),
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(Res.font.outfit_font)),
            fontSize = 16.sp
        )
    )
}

@Composable
fun EmptyComponent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_empty),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "Không tìm thấy liên hệ nào",
            fontFamily = FontFamily(Font(Res.font.outfit_font)),
            color = TextSecondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 15.dp)
        )
        Text(
            text = "Thử tìm kiếm với từ khóa khác hoặc thay đổi bộ lọc",
            fontFamily = FontFamily(Font(Res.font.outfit_font)),
            color = TextTertiary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFilterScreenPreview() {
    AppTheme { SearchFilterScreen() }
}


@Preview
@Composable
private fun UserProfilePreview() {
    val user = Contact(
        id = 1,
        name = "Nguyễn Văn An",
        phone = "0901 234 567",
        email = "an.nguyen@example.com",
        isFavorite = true,
        bio = "Yêu thích lập trình Android và Kotlin."
    )
    AppTheme {
        UserProfile(contact = user, modifier = Modifier.background(Color.White))
    }
}