package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.CalendarEventType

data class CalendarEntry(
    val day: Int,
    val eventCount: Int,
    val type: CalendarEventType,
    val title: String,
    val colorCode: String = ""
)