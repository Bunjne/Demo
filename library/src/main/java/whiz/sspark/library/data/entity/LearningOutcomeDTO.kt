package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class LearningOutcomeDTO(
    @SerializedName("headerEn") val headerEn: String = "",
    @SerializedName("headerTh") val headerTh: String = "",
    @SerializedName("headerCn") val headerCn: String = "",
    @SerializedName("gradientColor1") val gradientColor1: String = "",
    @SerializedName("gradientColor2") val gradientColor2: String = "",
    @SerializedName("courses") private val _courses: List<LearningOutcomeCourseDTO>? = null
) {
    val header get() = localize(headerEn, headerTh, headerCn, false)
    val courses get() = _courses ?: listOf()
}

data class LearningOutcomeCourseDTO(
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("nameCn") val nameCn: String = "",
    @SerializedName("credits") val credits: Int = 0,
    @SerializedName("percentPerformance") val percentPerformance: Int? = null
) {
    val name get() = localize(nameEn, nameTh, nameCn, false)
}