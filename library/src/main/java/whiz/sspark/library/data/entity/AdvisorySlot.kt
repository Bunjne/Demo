package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class AdvisorySlot(
    @SerializedName("id") val id: String = "",
    @SerializedName("endAt") val endAt: String = "",
    @SerializedName("startAt") val startAt: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("student") val student: Student? = null
)