package com.plavsic.taskly.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun String.toLocalDate() : LocalDate {
    return LocalDate.parse(this)
}

fun LocalDate.toFirebaseString() : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}
