package com.plavsic.taskly.utils.conversion

import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun String.toLocalDate() : LocalDate {
    return LocalDate.parse(this)
}

fun LocalDate.toFirebaseString() : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}

fun Int.to24HourFormat(isPM: Boolean): Int {
    return when {
        this == 12 && !isPM -> 0
        this == 12 && isPM -> 12
        isPM -> this + 12
        else -> this
    }
}