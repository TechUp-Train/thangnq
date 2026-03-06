package com.apero.composetraining.session1.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐ BÀI TẬP 2: Contact Card (Medium)
 *
 * Yêu cầu:
 * - Row chứa: Image (CircleShape, 60dp) + Column (Name bold + Bio regular)
 * - Button "Follow" ở dưới, fillMaxWidth
 * - Card có elevation, rounded corner 12dp
 * - Modifier chain ít nhất 3 modifier
 * - Dùng cả Column, Row, Box
 * - @Preview với showBackground = true
 */

@Composable
fun ContactCard(
    name: String = "Thang Nguyen Quang",
    bio: String = "Android Developer tại Apero"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // TODO: [Session 1] Bài tập 2 - Thêm avatar (Icon hoặc Image)
                // Gợi ý: Icon với modifier .size(60.dp).clip(CircleShape).background(...)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
                // TODO: [Session 1] Bài tập 2 - Thêm Column chứa Name (Bold) và Bio (Regular)
                // Gợi ý: Column(modifier = Modifier.weight(1f).padding(start = 12.dp))
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Thang Nguyen Quang",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Thang Nguyen Quang",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // TODO: [Session 1] Bài tập 2 - Thêm Button "Follow" với fillMaxWidth
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text(
                    "Follow",
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactCardPreview() {
    AppTheme { ContactCard() }
}
