package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.localize

data class MenuGradeSummaryDTO (
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("value") val value: Float = 0f,
    @SerializedName("fullValue") val fullValue: Float = 0f,
    @SerializedName("colorCode1") val colorCode1: String = "",
    @SerializedName("colorCode2") val colorCode2: String = "",
    @SerializedName("courses") private val _courses: List<MenuGradeSummaryCourseDTO>? = null
) {
    val name get() = localize(nameEn, nameTh, nameEn, false)
    val courses get() = _courses ?: listOf()
}

data class MenuGradeSummaryCourseDTO (
    @SerializedName("code") val code: String = "",
    @SerializedName("nameEn") val nameEn: String = "",
    @SerializedName("nameTh") val nameTh: String = "",
    @SerializedName("nameCn") val nameCn: String = "",
    @SerializedName("credits") val credits: Int = 0,
    @SerializedName("value") val value: Float = 0f,
    @SerializedName("fullValue") val fullValue: Float = 0f
)

fun convertToGradeSummaryItem(grades: List<MenuGradeSummaryDTO>): List<GradeSummary> {
    return grades.map { GradeSummary(name = it.name, startColorCode = it.colorCode1, endColorCode = it.colorCode2, grade = it.value) }
}