package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class OnlineClass(
        @SerializedName("sectionId") val sectionId: Long = 0L,
        @SerializedName("classGroupId") val classGroupId: String = "",
        @SerializedName("nameEn") val nameEn: String = "",
        @SerializedName("onlineClassUrl") val onlineClassUrl: String = "",
        @SerializedName("startedAt") val startedAt: Date? = Date(),
        @SerializedName("endedAt") val endedAt: Date? = Date()
)