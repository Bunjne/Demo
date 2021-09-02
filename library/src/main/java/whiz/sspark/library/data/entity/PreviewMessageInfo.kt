package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class PreviewMessageInfo(
    @SerializedName("screen") val screen: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("notificationCount") val notificationCount: Int = 0,
    @SerializedName("date") val date: Date? = null
)