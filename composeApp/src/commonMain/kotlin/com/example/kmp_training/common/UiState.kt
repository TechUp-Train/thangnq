package com.example.kmp_training.common

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val codeError: Int? = null, val message: String) : UiState<Nothing>()
}