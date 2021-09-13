package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class MenuAdvisingNoteDTO (
    @SerializedName("unreadMessageCount") val unreadMessageCount: Int = 0,
    @SerializedName("message") val message: String = "",
    @SerializedName("createdAt") val createdAt: Date? = null,
    @SerializedName("advisor") val advisor: StudentInstructorDTO? = null
) {
    fun convertToPreviewMessageItem(): PreviewMessageItem {
        return PreviewMessageItem(
            title = advisor?.fullName ?: "",
            description = message,
            notificationCount = unreadMessageCount,
            date = createdAt
        )
    }
}