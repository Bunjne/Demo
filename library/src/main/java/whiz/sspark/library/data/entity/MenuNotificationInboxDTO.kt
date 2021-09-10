package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class MenuNotificationInboxDTO (
    @SerializedName("unreadMessageCount") val unreadMessageCount: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("createdAt") val createdAt: Date? = null
) {
    fun convertToPreviewMessageItem(): PreviewMessageItem {
        return PreviewMessageItem(
            title = title,
            description = message,
            notificationCount = unreadMessageCount,
            date = createdAt
        )
    }
}