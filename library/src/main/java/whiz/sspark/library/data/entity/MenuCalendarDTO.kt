package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class MenuCalendarDTO (
    @SerializedName("date") val date: Date? = null,
    @SerializedName("events") private val _events: List<MenuCalendarEventDTO>? = null
) {
    val events get() = _events ?: listOf()

    fun convertToCalendarItem(): CalendarWidgetItem {
        return CalendarWidgetItem(
            date = date ?: Date(),
            title = events.firstOrNull()?.name ?: ""
        )
    }
}

data class MenuCalendarEventDTO (
    @SerializedName("fromDate") val fromDate: Date = Date(),
    @SerializedName("toDate") val toDate: Date = Date(),
    @SerializedName("type") val type: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("name") val name: String = ""
)