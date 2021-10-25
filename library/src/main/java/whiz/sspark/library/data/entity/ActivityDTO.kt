package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class ActivityDTO(
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("activities") private val _activities: List<ActivityItemDTO>? = null
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
    val activities get() = _activities ?: listOf()
}

data class ActivityItemDTO(
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("comment") val comment: String = "",
    @SerializedName("isCompleted") val isCompleted: Boolean? = null,
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
}
