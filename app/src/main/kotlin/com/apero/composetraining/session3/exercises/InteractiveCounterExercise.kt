package com.apero.composetraining.session3.exercises

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.R

/**
 * ⭐ BÀI TẬP 1: Interactive Counter (Easy)
 *
 * Yêu cầu:
 * - Text hiển thị count (fontSize 48sp)
 * - Row: Button "-" | Button "+" | Button "Reset"
 * - Count không được < 0
 * - Dùng remember + mutableStateOf
 * - Rotate thiết bị → count vẫn giữ (rememberSaveable)
 */

@Composable
fun InteractiveCounterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_page))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Interactive Counter",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.outfit_font)),
            color = colorResource(R.color.text_secondary),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: [Session 3] Bài tập 1 - Tạo biến count dùng rememberSaveable
        // var count by rememberSaveable { mutableIntStateOf(0) }
        var count by rememberSaveable { mutableIntStateOf(0) }
        // TODO: [Session 3] Bài tập 1 - Hiển thị count với fontSize 48sp
        Text(
            text = "$count",
            fontSize = 72.sp,
            fontFamily = FontFamily(Font(R.font.outfit_font)),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // TODO: [Session 3] Bài tập 1 - Tạo Row chứa 3 buttons: "-", "+", "Reset"
        // Button "-": giảm count (nhưng không < 0)
        // Button "+": tăng count
        // Button "Reset": đặt count = 0
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
            ) {
                OutlinedButton(
                    onClick = {
                        if (count > 0) count--
                    },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colorResource(R.color.disabled_gray)
                    )
                ) {
                    Text(
                        "-",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.disabled_gray),
                        fontFamily = FontFamily(Font(R.font.outfit_font))
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { count++ },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary_blue))
                ) {
                    Text(
                        "+",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.outfit_font))
                    )
                }
            }
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = colorResource(R.color.border_strong)
                ),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    "Reset",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.warm_red),
                    fontFamily = FontFamily(Font(R.font.outfit_font))
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractiveCounterScreenPreview() {
    AppTheme { InteractiveCounterScreen() }
}
