package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class HighlightHeader(
        @SerializedName("type") val type: Int = 0,
        @SerializedName("level") val level: String = "",
        @SerializedName("title")  val title: String = ""
)