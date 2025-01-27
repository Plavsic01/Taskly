package com.plavsic.taskly.utils.conversion


// Used for converting from ULong to Long and vice versa because Room db only accepts Long

// Used for Color(color:ULong)

fun uLongToLong(value:ULong) : Long {
    return value.toLong()
}

fun longToULong(value:Long) : ULong {
    return value.toULong()
}