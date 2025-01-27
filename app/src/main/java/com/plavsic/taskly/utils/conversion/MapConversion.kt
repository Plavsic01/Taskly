package com.plavsic.taskly.utils.conversion

import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.ui.shared.task.TaskPriority

fun Map<*,*>.toPriority() : TaskPriority {
    val number = this["number"] as Long
    return when(number){
        1L -> TaskPriority.FirstPriority
        2L -> TaskPriority.SecondPriority
        3L -> TaskPriority.ThirdPriority
        4L -> TaskPriority.ForthPriority
        5L -> TaskPriority.FifthPriority
        6L -> TaskPriority.SixthPriority
        7L -> TaskPriority.SeventhPriority
        8L -> TaskPriority.EightPriority
        9L -> TaskPriority.NinthPriority
        10L -> TaskPriority.TenthPriority
        else -> {
            TaskPriority.FirstPriority}
    }
}


fun Map<*,*>.toCategory() : Category {
    val id = this["id"] as Long
    val image = this["image"] as String
    val name  = this["name"] as String
    val color = this["color"] as Long

    return Category(id,image,name,color)
}