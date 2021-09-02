package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class MenuItem(
    @SerializedName("iconUrl") val iconUrl: String = "",
    @SerializedName("titleEn") val titleEn: String = "",
    @SerializedName("titleTh") private val titleTh: String = "",
    @SerializedName("titleCn") private val titleCn: String = "",
    @SerializedName("subTitle") val subTitle: String = "",
    @SerializedName("navigateToScene") val navigateToScene: String = "",
    @SerializedName("messageNotEnable") val messageNotEnable: String = "",
    @SerializedName("isEnable") val isEnable: Boolean = false,
)
