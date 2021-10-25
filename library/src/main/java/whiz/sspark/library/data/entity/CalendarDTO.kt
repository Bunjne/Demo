package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class CalendarDTO(
        @SerializedName("month") val month: Int = 0,
        @SerializedName("year") val year: Int = 0,
        @SerializedName("events") private val _events: List<CalendarEventDTO>? = null
) {
        val events get() = _events ?: listOf()
}

data class CalendarEventDTO(
        @SerializedName("fromDate") val fromDate: Date = Date(),
        @SerializedName("toDate")val toDate: Date = Date(),
        @SerializedName("type")val type: String = "",
        @SerializedName("name")val name: String = "",
        @SerializedName("colorCode")val colorCode: String = ""
)