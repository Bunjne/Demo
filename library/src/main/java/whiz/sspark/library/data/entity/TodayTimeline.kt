package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class TimelineResponse (
    @SerializedName("day") val day: String = "",
    @SerializedName("aqius") val aqi: Int = 0,
    @SerializedName("aqiIcon") val aqiIcon: String = "",
    @SerializedName("aqiColor") val aqiColor: String = "",
    @SerializedName("weatherIcon") val weatherIcon: String? = "",
    @SerializedName("tempHi") val tempHi: Double = 0.0,
    @SerializedName("tempLw") val tempLw: Double = 0.0,
    @SerializedName("alertAnnouncements") val _alertAnnouncements: List<String> = listOf(),
    @SerializedName("items") private val _item: List<TimelineItem>? = listOf(),
    @SerializedName("backgroundColor") val backgroundColor: String,
    @SerializedName("dayImageUrl") val dayImageUrl: String? = "",
    @SerializedName("nightImageUrl") val nightImageUrl: String? = ""
) {
    val item get() = _item ?: listOf()
    val alertAnnouncements get() = _alertAnnouncements ?: listOf()
}

data class TimelineItem (
    @SerializedName("startTime") val startTime: String = "",
    @SerializedName("endTime") val endTime: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("linkTo") val linkTo: String = "",
    @SerializedName("iconUrl") val icon: String = "",
    @SerializedName("order") val order: Int = 0,
    @SerializedName("backgroundColor") val backgroundColor: String = "",
    @SerializedName("body") private val _body: List<TimelineItemBody>? = listOf()
) {
    val body get() = _body ?: listOf()
}

data class TimelineItemBody (
    @SerializedName("order") val order: Int = 0,
    @SerializedName("text") val text: String = "",
    @SerializedName("color") val color: String = "",
    @SerializedName("style") val style: String = "",
    @SerializedName("icon") val icon: String = ""
)