package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class MenuAdvisingNoteDTO (
    @SerializedName("unreadMessageCount") val unreadMessageCount: Int = 0,
    @SerializedName("note") val note: MenuAdvisingNoteDetailDTO = MenuAdvisingNoteDetailDTO(),
) {
    fun convertToPreviewMessageItem(): PreviewMessageItem {
        return PreviewMessageItem(
            title = note.advisor?.fullName ?: "",
            description = note.message,
            notificationCount = unreadMessageCount,
            date = note.dateTime
        )
    }
}

data class MenuAdvisingNoteDetailDTO(
    @SerializedName("message") val message: String = "",
    @SerializedName("dateTime") val dateTime: Date? = null,
    @SerializedName("advisor") val advisor: StudentInstructorDTO? = null
)