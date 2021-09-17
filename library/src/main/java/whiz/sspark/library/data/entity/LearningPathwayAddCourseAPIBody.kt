package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class LearningPathwayAddCourseAPIBody(
    @SerializedName("termId") val termId: String,
    @SerializedName("courseId") val courseId: String
)