package com.apero.composetraining.session4.exercises.staggeredgallery.extension

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

object Extensions {
    fun String.calculateDeterministicHeight(): Dp {
        return (Random.nextInt(100, 300)).dp
    }
}