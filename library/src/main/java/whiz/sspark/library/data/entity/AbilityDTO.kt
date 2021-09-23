package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class AbilityDTO(
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("abilities") private val _abilities: List<AbilityItemDTO>? = null
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
    val abilities get() = _abilities ?: listOf()
}

data class AbilityItemDTO(
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("value") val value: Float = 0f,
    @SerializedName("fullValue") val fullValue: Float = 0f,
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
}
