package whiz.sspark.library.data.enum

enum class TimeLineItemType(val type: String) {
    ALERT("alert"),
    CALENDAR("calendar"),
    CLASS("CLASS_DETAIL"),
    APPOINTMENT("ADVISING_APPOINTMENT"),
    EVENT("EVENT_DETAIL"),
    REGISTRATION_TIME_SLOT("REGISTRATION_TIME_SLOT"),
    OTHER("other")
}