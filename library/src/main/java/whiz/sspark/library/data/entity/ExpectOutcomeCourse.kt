package whiz.sspark.library.data.entity

data class ExpectOutcomeCourse(
    val title: String,
    val description: String,
    val value: Float,
    val startColor: Int,
    val endColor: Int,
    val indicators: List<String>
)
