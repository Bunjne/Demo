package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class MenuCalendarDTO (
    @SerializedName("createdAt") val createdAt: Date? = null,
    @SerializedName("events") private val _events: List<MenuCalendarEventDTO>? = null
) {
    val events get() = _events ?: listOf()

    fun convertToCalendarItem(): CalendarWidgetItem {
        return CalendarWidgetItem(
            date = createdAt,
            title = events.firstOrNull()?.name ?: ""
        )
    }
}

data class MenuCalendarEventDTO (
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = ""
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
}