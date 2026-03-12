package com.example.kmp_training.lession1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmp_training.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP 3: Mini Profile Screen (Challenge)
 *
 * Yêu cầu:
 * - Header: Avatar + Name + "Edit Profile" button
 * - Stats Row: 3 cột (Posts | Followers | Following) dùng Row + weight
 * - Bio section: Text nhiều dòng
 * - Action buttons: "Message" + "Follow" ngang hàng
 * - Bonus: Thêm Spacer hợp lý, đúng alignment
 */

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }

            Column() {
                Text(
                    text = "Thang Nguyen Quang",
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = {}) {
                    Text(text = "Edit Profile")
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DetailProfile(data = "Posts (128)", modifier = Modifier.weight(1f))
            DetailProfile(data = "Followers (1.2K)", modifier = Modifier.weight(1f))
            DetailProfile(data = "Following (380)", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Android Developer \nLover of clean code\nHà Nội, Việt Nam")
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("Message")
            }

            Button(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("Follow")
            }
        }
    }
}

@Composable
fun DetailProfile(data: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = data)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    AppTheme { ProfileScreen() }
}
