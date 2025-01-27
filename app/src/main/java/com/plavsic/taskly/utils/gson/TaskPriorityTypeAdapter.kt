package com.plavsic.taskly.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import com.google.gson.stream.JsonReader
import com.plavsic.taskly.ui.shared.task.TaskPriority

class TaskPriorityTypeAdapter : TypeAdapter<TaskPriority>() {
    override fun write(out: JsonWriter, value: TaskPriority) {
        out.beginObject()
        out.name("type").value(value::class.simpleName)
        out.name("number").value(value.number)
        out.endObject()
    }

    override fun read(reader: JsonReader): TaskPriority {
        var type: String? = null
        var number: Int? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "type" -> type = reader.nextString()
                "number" -> number = reader.nextInt()
            }
        }
        reader.endObject()

        return when (type) {
            "FirstPriority" -> TaskPriority.FirstPriority
            "SecondPriority" -> TaskPriority.SecondPriority
            "ThirdPriority" -> TaskPriority.ThirdPriority
            "ForthPriority" -> TaskPriority.ForthPriority
            "FifthPriority" -> TaskPriority.FifthPriority
            "SixthPriority" -> TaskPriority.SixthPriority
            "SeventhPriority" -> TaskPriority.SeventhPriority
            "EightPriority" -> TaskPriority.EightPriority
            "NinthPriority" -> TaskPriority.NinthPriority
            "TenthPriority" -> TaskPriority.TenthPriority
            else -> throw IllegalArgumentException("Unknown TaskPriority type: $type")
        }
    }
}
