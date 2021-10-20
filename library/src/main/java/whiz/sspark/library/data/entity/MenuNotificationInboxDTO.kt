package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class MenuNotificationInboxDTO (
    @SerializedName("notReadNotificationCount") val notReadNotificationCount: Int = 0,
    @SerializedName("item") val item: MenuNotificationInboxItemDTO? = null
) {
    fun convertToPreviewMessageItem()= PreviewMessageItem(
        title = item?.title ?: "",
        description = item?.message ?: "",
        notificationCount = notReadNotificationCount,
        date = item?.datetime
    )
}

data class MenuNotificationInboxItemDTO(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("isRead") val isRead: Boolean = false,
    @SerializedName("datetime") val datetime: Date? = null
)