package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class News(
        @SerializedName("level") val level: String = "",
        @SerializedName("levelNameEn") val _levelNameEn: String = "",
        @SerializedName("levelNameTh") val _levelNameTh: String = "",
        @SerializedName("iconUrl") val iconUrl: String = "",
        @SerializedName("news") val news: List<NewsDetail> = listOf()
) {
    val levelName: String get() = localize(_levelNameEn, _levelNameTh, _levelNameEn)
}

data class NewsList(
        @SerializedName("currentPage") val currentPage: Int = 0,
        @SerializedName("totalPage") val totalPage: Int = 0,
        @SerializedName("totalItem") val totalItem: Int = 0,
        @SerializedName("items") val items: List<NewsDetail> = listOf()
)