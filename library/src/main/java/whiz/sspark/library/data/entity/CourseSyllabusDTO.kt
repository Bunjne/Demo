package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import whiz.sspark.library.utility.convertToFullName
import whiz.sspark.library.utility.localize

data class CourseSyllabusDTO(
    @SerializedName("descriptionEn") val descriptionEn: String = "",
    @SerializedName("descriptionTh") val descriptionTh: String = "",
    @SerializedName("outcomes") private val _outcomes: List<CourseSyllabusCourseDTO>? = null,
    @SerializedName("syllabus") private val _syllabus: List<CourseSyllabusWeekDTO>? = null,
) {
    val description: String get() = localize(descriptionEn, descriptionTh, descriptionEn, false)
    val outcomes get() = _outcomes ?: listOf()
    val syllabus get() = _syllabus ?: listOf()
}

data class CourseSyllabusCourseDTO(
    @SerializedName("code") val code: String = "",
    @SerializedName("descriptionEn") val descriptionEn: String = "",
    @SerializedName("descriptionTh") val descriptionTh: String = ""
) {
    val description: String get() = localize(descriptionEn, descriptionTh, descriptionEn, false)
}

data class CourseSyllabusWeekDTO(
    @SerializedName("week") val week: Int = 0,
    @SerializedName("topics") private val _topics: List<String>? = null,
    @SerializedName("instructors") val instructors: List<CourseSyllabusInstructorDTO> = listOf()
) {
    val topics get() = _topics ?: listOf()
}

data class CourseSyllabusInstructorDTO(
    @SerializedName("abbreviateNameEn") val abbreviateNameEn: String = "",
    @SerializedName("abbreviateNameTh") val abbreviateNameTh: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("profileImageUrl") val profileImageUrl: String? = null,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("firstNameEn") val firstNameEn: String = "",
    @SerializedName("firstNameTh") val firstNameTh: String = "",
    @SerializedName("middleNameEn") val middleNameEn: String = "",
    @SerializedName("middleNameTh") val middleNameTh: String = "",
    @SerializedName("lastNameEn") val lastNameEn: String = "",
    @SerializedName("lastNameTh") val lastNameTh: String = "",
    @SerializedName("positionEn") val positionEn: String = "",
    @SerializedName("positionTh") val positionTh: String = "",
) {
    val firstName: String get() = localize(firstNameEn, firstNameTh, firstNameEn, false)
    val position: String get() = localize(positionEn, positionTh, positionEn, false)

    val firstNameWithPosition get() = convertToFullName(firstName, null, null, position)
}