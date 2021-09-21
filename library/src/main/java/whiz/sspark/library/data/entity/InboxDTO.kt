package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class InboxDTO(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("navigation") val navigation: String,
    @SerializedName("navigationId") val navigationId: String,
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("datetime") val datetime: Date
)