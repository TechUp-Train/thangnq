package com.example.kmp_training.lession3

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.common.BgPage
import com.example.kmp_training.common.BorderStrong
import com.example.kmp_training.common.DisabledGray
import com.example.kmp_training.common.PrimaryBlue
import com.example.kmp_training.common.TextSecondary
import com.example.kmp_training.common.WarmRed
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.outfit_font
import org.jetbrains.compose.resources.Font

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
            .background(color = BgPage)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Interactive Counter",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(Res.font.outfit_font)),
            color = TextSecondary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        var count by rememberSaveable { mutableIntStateOf(0) }
        Text(
            text = "$count",
            fontSize = 72.sp,
            fontFamily = FontFamily(Font(Res.font.outfit_font)),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

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
                        color = DisabledGray
                    )
                ) {
                    Text(
                        "-",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DisabledGray,
                        fontFamily = FontFamily(Font(Res.font.outfit_font))
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { count++ },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        "+",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(Res.font.outfit_font))
                    )
                }
            }
            OutlinedButton(
                onClick = { count = 0 },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = BorderStrong
                ),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    "Reset",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = WarmRed,
                    fontFamily = FontFamily(Font(Res.font.outfit_font))
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
