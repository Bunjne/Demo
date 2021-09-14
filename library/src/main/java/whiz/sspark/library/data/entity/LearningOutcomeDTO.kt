package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class LearningOutcomeDTO(
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("value") val value: Float? = 3f,
    @SerializedName("fullValue") val fullValue: Float = 4f,
    @SerializedName("colorCode1") val colorCode1: String = "",
    @SerializedName("colorCode2") val colorCode2: String = "",
    @SerializedName("courses") private val _courses: List<LearningOutcomeCourseDTO>? = null
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
    val courses get() = _courses ?: listOf()
}

data class LearningOutcomeCourseDTO(
    @SerializedName("id") val id: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("nameCn") val nameCn: String = "",
    @SerializedName("isCompleted") val isCompleted: Boolean = false,
    @SerializedName("credits") val credits: Int = 0,
    @SerializedName("value") val value: Float? = null,
    @SerializedName("fullValue") val fullValue: Float? = null
) {
    val name get() = localize(nameEn, nameTh, nameCn, false)
    val percentPerformance get() = if (value != null && fullValue != null) {
        ((value / fullValue) * 100).toInt()
    } else {
       null
    }
}