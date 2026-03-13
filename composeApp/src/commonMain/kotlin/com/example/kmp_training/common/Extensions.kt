package com.example.kmp_training.common

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

object Extensions {
    fun Long.formatTimestamp(): String {
        val instant = Instant.fromEpochMilliseconds(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.hour.toString().padStart(2, '0')}:${
            localDateTime.minute.toString().padStart(2, '0')
        } ${localDateTime.dayOfMonth}/${localDateTime.month.number}/${localDateTime.year}"
    }
}
