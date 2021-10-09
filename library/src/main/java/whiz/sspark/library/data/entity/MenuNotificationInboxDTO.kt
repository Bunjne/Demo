package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class MenuNotificationInboxDTO (
    @SerializedName("totalPage") val totalPage: Int = 0,
    @SerializedName("items") private val _items: List<MenuNotificationInboxItemDTO>? = null
) {
    val item get() = _items ?: listOf()

    fun convertToPreviewMessageItem(): PreviewMessageItem {
        val unreadNotification = item.firstOrNull { !it.isRead }
        val unreadMessageCount = item.filterNot { it.isRead }.size

        return PreviewMessageItem(
            title = unreadNotification?.title ?: "",
            description = unreadNotification?.message ?: "",
            notificationCount = unreadMessageCount,
            date = unreadNotification?.datetime
        )
    }
}

data class MenuNotificationInboxItemDTO(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("isRead") val isRead: Boolean = false,
    @SerializedName("datetime") val datetime: Date? = null
)