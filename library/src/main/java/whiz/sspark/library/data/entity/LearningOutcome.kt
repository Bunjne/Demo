package whiz.sspark.library.data.entity

data class LearningOutcome(
    val startColor: Int = 0,
    val endColor: Int? = null,
    val credit: Int = 0,
    val percentPerformance: Int? = null,
    val courseCode: String = "",
    val courseName: String = ""
)