package com.example.kmp_training.lession4.staggeredgallery.extension

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

object Extensions {
    fun String.calculateDeterministicHeight(): Dp {
        return (Random.nextInt(100, 300)).dp
    }
}