package com.apero.composetraining.session3.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Interactive Counter", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: [Session 3] Bài tập 1 - Tạo biến count dùng rememberSaveable
        // var count by rememberSaveable { mutableIntStateOf(0) }

        // TODO: [Session 3] Bài tập 1 - Hiển thị count với fontSize 48sp

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: [Session 3] Bài tập 1 - Tạo Row chứa 3 buttons: "-", "+", "Reset"
        // Button "-": giảm count (nhưng không < 0)
        // Button "+": tăng count
        // Button "Reset": đặt count = 0
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractiveCounterScreenPreview() {
    AppTheme { InteractiveCounterScreen() }
}
