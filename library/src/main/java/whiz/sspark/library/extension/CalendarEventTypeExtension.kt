package whiz.sspark.library.extension

import whiz.sspark.library.data.enum.CalendarEventType

fun String.toCalendarEventType() = when(this) {
    "E" -> CalendarEventType.EXAM
    "H" -> CalendarEventType.HOLIDAY
    "S" -> CalendarEventType.SEMESTER
    "N" -> CalendarEventType.GENERAL
    else -> CalendarEventType.GENERAL
}
