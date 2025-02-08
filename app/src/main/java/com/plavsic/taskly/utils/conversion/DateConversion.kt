package com.plavsic.taskly.utils.conversion

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun LocalDateTime.formatDateTime() : String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    val formattedTime = this.format(formatter)
    return formattedTime
}

fun LocalDate.formatDate() : String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    val formattedTime = this.format(formatter)
    return formattedTime
}

fun LocalTime.formatTime() : String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    val formattedTime = this.format(formatter)
    return formattedTime
}

fun Int.to24HourFormat(isPM: Boolean): Int {
    return when {
        this == 12 && !isPM -> 0
        this == 12 && isPM -> 12
        isPM -> this + 12
        else -> this
    }
}