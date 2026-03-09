package com.apero.composetraining.session5.exercises.auth_exercise.auth_flow

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.apero.composetraining.session5.exercises.auth_exercise.AuthKey

@Composable
fun AuthFlow(
    onLoginSuccess: () -> Unit
) {
    val backStack = rememberNavBackStack(AuthKey.Login)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<AuthKey.Login> {
                LoginScreen(
                    onLogin = onLoginSuccess,
                    onRegister = { backStack.add(AuthKey.Register) },
                    onForgotPassword = { backStack.add(AuthKey.ForgotPassword) }
                )
            }

            entry<AuthKey.Register> {
                RegisterScreen(
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<AuthKey.ForgotPassword> {
                var showDialog by remember { mutableStateOf(false) }

                BackHandler {
                    showDialog = true
                }

                ForgotPasswordScreen(
                    onBack = { showDialog = true }
                )

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Quay lại?") },
                        text = { Text("Bạn có chắc muốn quay lại không?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    if (backStack.size > 1) {
                                        backStack.removeLastOrNull()
                                    }
                                }
                            ) {
                                Text("Đồng ý")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Ở lại")
                            }
                        }
                    )
                }
            }
        }
    )
}