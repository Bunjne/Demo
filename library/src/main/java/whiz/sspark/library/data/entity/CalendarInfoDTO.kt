package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class CalendarInfoDTO(
        @SerializedName("type") val type: String = "",
        @SerializedName("name") val name: String = "",
        @SerializedName("colorCode") val colorCode: String = "",
)