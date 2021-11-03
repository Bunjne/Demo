package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class LearningPathwayDTO(
    @SerializedName("term") val term: Term,
    @SerializedName("minCredit") val minCredit: Int = 0,
    @SerializedName("maxCredit") val maxCredit: Int = 0,
    @SerializedName("courses") private val _courses: List<CourseDTO>?,
    @SerializedName("basicCourses") private val _basicCourses: List<CourseDTO>?,
) {
    val courses get() = _courses ?: listOf()
    val basicCourses get() = _basicCourses ?: listOf()
}