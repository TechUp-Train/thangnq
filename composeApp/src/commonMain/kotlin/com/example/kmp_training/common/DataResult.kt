package com.example.kmp_training.common

sealed class DataResult<out T> {
    data object Loading : DataResult<Nothing>()
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error(val exception: Throwable, val message: String? = exception.message) : DataResult<Nothing>()
}
