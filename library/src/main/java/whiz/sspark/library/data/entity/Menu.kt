package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class MenuDTO(
    @SerializedName("currentSemester") private val _currentSemester: Semester? = Semester(),
    @SerializedName("advisingSemester") private val _advisingSemester: Semester? = Semester(),
    @SerializedName("sections") private val _menus: List<MenuSectionDTO>? = listOf()
)

data class MenuSectionDTO(
    @SerializedName("headerEn") val headerEn: String = "",
    @SerializedName("headerTh") private val headerTh: String = "",
    @SerializedName("headerCn") private val headerCn: String = "",
    @SerializedName("menus") private val _items: List<MenuSectionItemDTO>? = null
) {
    val items: List<MenuSectionItemDTO> get() = _items ?: listOf()
    val header: String get() = localize(headerEn, headerTh, headerCn, false)
}

data class MenuSectionItemDTO(
    @SerializedName("iconUrl") val iconUrl: String = "",
    @SerializedName("titleEn") val titleEn: String = "",
    @SerializedName("titleTh") private val titleTh: String = "",
    @SerializedName("titleCn") private val titleCn: String = "",
    @SerializedName("subTitle") val subTitle: String = "",
    @SerializedName("navigateToScene") val navigateToScene: String = "",
    @SerializedName("messageNotEnable") val messageNotEnable: String = "",
    @SerializedName("isEnable") val isEnable: Boolean = false,
    @SerializedName("widgetType") val widgetType: String = "",
    @SerializedName("widgetDetail") val widgetDetail: String = "",
    @SerializedName("order") val order: Long = 0,
    @SerializedName("notificationCount") val notificationCount: Int = 0
) {
    val title: String get() = localize(titleEn, titleTh, titleCn)
}