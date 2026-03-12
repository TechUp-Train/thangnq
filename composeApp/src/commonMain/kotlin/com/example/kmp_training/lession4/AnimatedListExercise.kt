package com.example.kmp_training.lession4

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kmp_training.common.AppTheme

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 4: Animated
 *
 * Mô tả: LazyColumn với animations khi thêm/xóa/reorder items
 *
 * ┌───────────────────────────────────────┐
 * │ Animated            │
 * ├───────────────────────────────────────┤
 * │ ── Active Tasks ─────────────────── │  ← stickyHeader
 * │ □ Design mockup          [↑][↓][🗑] │
 * │ □ Code review            [↑][↓][🗑] │
 * ├───────────────────────────────────────┤
 * │ ── Completed Tasks ─────────────── │  ← stickyHeader
 * │ ✓ Write tests            [↑][↓][🗑] │
 * └───────────────────────────────────────┘
 *                                  [+ FAB]  ← ẩn khi scroll xuống
 *
 * Key concepts:
 * - animateItem(): Compose 1.7+ API, tự động animate placement khi list thay đổi
 * - stickyHeader {}: Header dính trên cùng khi scroll qua section
 * - rememberLazyListState() + derivedStateOf: track scroll direction → FAB show/hide
 */

// ─── Data Model ──────────────────────────────────────────────────────────────

data class TodoItem(
    val id: Int,
    val title: String,
    val isDone: Boolean = false,
)

// ─── Main Screen ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedListScreen(modifier: Modifier = Modifier) {
    var todos by rememberSaveable {
        mutableStateOf(
            listOf(
                TodoItem(1, "Design mockup"),
                TodoItem(2, "Code review", isDone = true),
                TodoItem(3, "Code review"),
                TodoItem(4, "Code review"),
                TodoItem(5, "Code review"),
                TodoItem(6, "Code review", isDone = true),
                TodoItem(7, "Code review"),
                TodoItem(8, "Code review"),
                TodoItem(9, "Code review"),
                TodoItem(10, "Code review"),
                TodoItem(11, "Code review"),
                TodoItem(12, "Write tests", isDone = true)
            )
        )
    }
    var newTodoText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var nextId by remember { mutableStateOf(4) }

    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    val activeTodos = todos.filter { !it.isDone }
    val completedTodos = todos.filter { it.isDone }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                ExtendedFloatingActionButton(
                    onClick = { },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Task") },
                    text = { Text("New Task") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AddTodoInput(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                onAdd = {
                    if (newTodoText.isNotBlank()) {
                        todos = listOf(TodoItem(nextId++, newTodoText)) + todos
                        newTodoText = ""
                    }
                },
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (activeTodos.isNotEmpty()) {
                    stickyHeader(key = "active_header") {
                        SectionHeader("Active Tasks")
                    }
                    itemsIndexed(activeTodos, key = { _, todo -> todo.id }) { index, todo ->
                        TodoRow(
                            todo = todo,
                            onToggle = {
                                todos =
                                    todos.map { if (it.id == todo.id) it.copy(isDone = !todo.isDone) else it }
                            },
                            onDelete = { todos = todos.filter { it.id != todo.id } },
                            onMoveUp = {
                                val index = todos.indexOf(todo)
                                if (index > 0) {
                                    val newTodos = todos.toMutableList()
                                    newTodos[index] = newTodos[index - 1]
                                    newTodos[index - 1] = todo
                                    todos = newTodos
                                }
                            },
                            onMoveDown = {
                                val index = todos.indexOf(todo)
                                if (index < todos.size - 1) {
                                    val newTodos = todos.toMutableList()
                                    newTodos[index] = newTodos[index + 1]
                                    newTodos[index + 1] = todo
                                    todos = newTodos
                                }
                            },
                            enableMoveUp = index > 0,
                            enableMoveDown = index < activeTodos.size - 1,
                            modifier = Modifier.animateItem()
                        )
                    }
                }

                if (completedTodos.isNotEmpty()) {
                    stickyHeader(key = "completed_header") {
                        SectionHeader("Completed Tasks")
                    }
                    itemsIndexed(completedTodos, key = { _, todo -> todo.id }) { index, todo ->
                        TodoRow(
                            todo = todo,
                            onToggle = {
                                todos =
                                    todos.map { if (it.id == todo.id) it.copy(isDone = !todo.isDone) else it }
                            },
                            onDelete = { todos = todos.filter { it.id != todo.id } },
                            onMoveUp = {
                                val index = todos.indexOf(todo)
                                if (index > 0) {
                                    val newTodos = todos.toMutableList()
                                    newTodos[index] = newTodos[index - 1]
                                    newTodos[index - 1] = todo
                                    todos = newTodos
                                }
                            },
                            onMoveDown = {
                                val index = todos.indexOf(todo)
                                if (index < todos.size - 1) {
                                    val newTodos = todos.toMutableList()
                                    newTodos[index] = newTodos[index + 1]
                                    newTodos[index + 1] = todo
                                    todos = newTodos
                                }
                            },
                            enableMoveUp = index > 0,
                            enableMoveDown = index < completedTodos.size - 1,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

// ─── Section Header (Sticky) ──────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun TodoRow(
    todo: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    enableMoveUp: Boolean,
    enableMoveDown: Boolean,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = if (todo.isDone) 0.5f else 1f,
        label = "todo_alpha"
    )

    Column(modifier = modifier.alpha(alpha)) {
        ListItem(
            headlineContent = {
                Text(
                    text = todo.title,
                    textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
                )
            },
            leadingContent = {
                Checkbox(
                    checked = todo.isDone,
                    onCheckedChange = { onToggle() }
                )
            },
            trailingContent = {
                Row {
                    IconButton(onClick = onMoveUp, enabled = enableMoveUp) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Move Up",
                            modifier = Modifier.rotate(90f)
                        )
                    }
                    IconButton(onClick = onMoveDown, enabled = enableMoveDown) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Move Down",
                            modifier = Modifier.rotate(90f)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            modifier = Modifier.background(
                if (todo.isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.surface
            )
        )
        HorizontalDivider()
    }
}

@Composable
private fun AddTodoInput(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = { Text("Add new task...") },
            shape = RoundedCornerShape(16.dp)
        )
        IconButton(
            onClick = onAdd,
            enabled = value.isNotBlank()
        ) {
            Icon(
                imageVector = if (value.isNotBlank()) Icons.Default.Check else Icons.Default.Add,
                contentDescription = "Add Task",
                tint = if (value.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Animated List - Light")
@Composable
private fun AnimatedListScreenPreview() {
    AppTheme {
        AnimatedListScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Animated List - Dark",
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun AnimatedListScreenDarkPreview() {
    AppTheme() {
        AnimatedListScreen()
    }
}

@Preview(showBackground = true, name = "Todo Row Preview")
@Composable
private fun TodoRowPreview() {
    AppTheme {
        Column {
            TodoRow(
                todo = TodoItem(1, "Design mockup", isDone = false),
                onToggle = {},
                onDelete = {},
                onMoveUp = {},
                onMoveDown = {},
                enableMoveUp = true,
                enableMoveDown = true
            )
            TodoRow(
                todo = TodoItem(2, "Write tests", isDone = true),
                onToggle = {},
                onDelete = {},
                onMoveUp = {},
                onMoveDown = {},
                enableMoveUp = true,
                enableMoveDown = true
            )
        }
    }
}
