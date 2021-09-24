package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class LearningPathwayDTO(
    @SerializedName("term") val term: Term,
    @SerializedName("minCredit") val minCredit: Int = 0,
    @SerializedName("maxCredit") val maxCredit: Int = 0,
    @SerializedName("course") private val _course: List<CourseDTO>?,
    @SerializedName("requiredCourses") private val _requiredCourses: List<CourseDTO>?,
) {
    val course get() = _course ?: listOf()
    val requiredCourses get() = _requiredCourses ?: listOf()
}