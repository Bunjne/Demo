package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize
import java.util.*

data class NewsDetail(
        @SerializedName("id") val id: String = "",
        @SerializedName("publisherId") val publisherId: String = "",
        @SerializedName("level") val level: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("description") val description: String = "",
        @SerializedName("coverImage") val coverImage: String = "",
        @SerializedName("attachment") val attachment: String = "",
        @SerializedName("content") val content: String = "",
        @SerializedName("isFlagged") val isFlagged: Boolean = false,
        @SerializedName("createdBy") val createdBy: String = "",
        @SerializedName("updatedBy") val updatedBy: String = "",
        @SerializedName("widgetOnClickedUrl") val widgetOnClickedUrl: String = "",
        @SerializedName("createdAt") val createdAt: Date = Date(),
        @SerializedName("updatedAt") val updatedAt: Date = Date(),
        @SerializedName("startedAt") val startedAt: Date = Date(),
        @SerializedName("expiredAt") val expiredAt: Date = Date(),
        @SerializedName("endTime") val endTime: String = "",
        @SerializedName("publisher") val publisher: NewsPublisher = NewsPublisher()
)

data class NewsPublisher(
        @SerializedName("id") val id: String = "",
        @SerializedName("nameEn") val _nameEn: String = "",
        @SerializedName("nameTh") val _nameTh: String = "",
        @SerializedName("imageName") val imageName: String = "",
        @SerializedName("createdAt") val createdAt: String = ""
) {
    val name: String get() = localize(_nameEn, _nameTh, _nameEn, false)
}