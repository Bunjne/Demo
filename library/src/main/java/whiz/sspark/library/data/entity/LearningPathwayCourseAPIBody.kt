package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class LearningPathwayCourseAPIBody(
    @SerializedName("term") val termId: Int,
    @SerializedName("academicGrade") val academicGrade: Int,
    @SerializedName("courseId") val courseId: String
)