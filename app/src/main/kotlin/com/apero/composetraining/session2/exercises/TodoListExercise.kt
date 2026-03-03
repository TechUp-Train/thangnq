package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData
import com.apero.composetraining.common.Todo

/**
 * ⭐ BÀI TẬP 1: Todo List (Easy)
 *
 * Yêu cầu:
 * - LazyColumn hiển thị danh sách 20 todo items
 * - Mỗi item: Checkbox + Text + Delete icon
 * - contentPadding = 16dp
 * - verticalArrangement = spacedBy(8dp)
 * - Key = todo.id
 */

@Composable
fun TodoListScreen() {
    val todos = SampleData.todos

    // TODO: [Session 2] Bài tập 1 - Tạo LazyColumn hiển thị danh sách todos
    // Gợi ý:
    // LazyColumn(
    //     contentPadding = PaddingValues(16.dp),
    //     verticalArrangement = Arrangement.spacedBy(8.dp)
    // ) {
    //     items(items = todos, key = { it.id }) { todo ->
    //         TodoItem(todo = todo)
    //     }
    // }

    // Placeholder — xóa khi bắt đầu làm
    Text("Bắt đầu code Todo List ở đây!", modifier = Modifier.padding(16.dp))
}

@Composable
fun TodoItem(todo: Todo) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // TODO: [Session 2] Bài tập 1 - Thêm Checkbox (checked = todo.isDone)

            // TODO: [Session 2] Bài tập 1 - Thêm Text hiển thị todo.title (dùng Modifier.weight(1f))

            // TODO: [Session 2] Bài tập 1 - Thêm IconButton delete (Icons.Default.Delete)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoListScreenPreview() {
    AppTheme { TodoListScreen() }
}
