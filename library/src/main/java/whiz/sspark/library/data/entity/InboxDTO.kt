package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class InboxDTO(
    @SerializedName("totalPage") val totalPage: Int = 0,
    @SerializedName("items") private val _items: List<InboxItemDTO>? = null
) {
    val item get() = _items ?: listOf()
}

data class InboxItemDTO(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("navigation") val navigation: String,
    @SerializedName("navigationId") val navigationId: String,
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("datetime") val datetime: Date
)