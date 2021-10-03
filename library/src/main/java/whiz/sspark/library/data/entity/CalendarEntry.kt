package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.CalendarEventType

data class CalendarEntry(
    val startDay: Int,
    val eventCount: Int,
    val type: CalendarEventType,
    val colorCode: String = ""
)