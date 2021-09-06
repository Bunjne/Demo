package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class MenuDTO(
    @SerializedName("code") val code: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") private val nameTh: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("items") val _items: List<MenuItemDTO>? = null
) {
    val item get() = _items ?: listOf()
}

data class MenuItemDTO(
    @SerializedName("code") val code: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("type") val type: String = "",
) {
    val name: String get() = localize(nameEn, nameTh, nameEn, false)
}