package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class CalendarWidgetItem(
    @SerializedName("title") val title: String = "",
    @SerializedName("date") val date: Date? = null
)