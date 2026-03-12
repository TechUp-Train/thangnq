package com.example.kmp_training

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform