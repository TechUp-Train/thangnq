package com.example.kmp_training.lession5.auth_exercise.auth_flow

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmp_training.lession5.auth_exercise.AuthKey
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Composable
fun AuthFlow(
    onLoginSuccess: () -> Unit
) {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(AuthKey.Login::class)
                subclass(AuthKey.Register::class)
                subclass(AuthKey.ForgotPassword::class)
            }
        }
    }
    val backStack = rememberNavBackStack(config, AuthKey.Login)

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

                NavigationBackHandler(
                    state = rememberNavigationEventState(NavigationEventInfo.None),
                    isBackEnabled = true,
                    onBackCompleted = {
                        showDialog = true
                    }
                )

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