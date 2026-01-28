package com.example.weatherpulse.feature_weather.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Formats a Unix timestamp into a user-friendly date string.
 * Example: "Mon - 2nd of January"
 */
fun Long.toFormattedDate(): String {
    val localDateTime = Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    val dayOfMonth = localDateTime.dayOfMonth
    val daySuffix = getDayOfMonthSuffix(dayOfMonth)

    val dayOfWeek = localDateTime.format(DateTimeFormatter.ofPattern("EEEE")).take(3)
    val month = localDateTime.format(DateTimeFormatter.ofPattern("MMMM"))

    return "$dayOfWeek - $dayOfMonth$daySuffix of $month"
}

private fun getDayOfMonthSuffix(day: Int): String {
    if (day in 11..13) {
        return "th"
    }
    return when (day % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}