package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class MenuDTO(
    @SerializedName("code") val code: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("imageUrl") val imageUrl: String = "",
    @SerializedName("items") private val _items: List<MenuItemDTO>? = null
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
    val items get() = _items ?: listOf()
}

data class MenuItemDTO(
    @SerializedName("code") val code: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("imageUrl") val imageUrl: String = ""
) {
    val name: String get() = localize(nameEn, nameTh, nameEn, false)

    fun convertToAdapterItem(): MenuItem {
        return MenuItem(code, name, imageUrl)
    }
}