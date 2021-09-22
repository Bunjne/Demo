package whiz.sspark.library.data.entity

data class LearningPathwayHeaderItem(
    val term: Term,
    val currentCredit: Int,
    val minCredit: Int,
    val maxCredit: Int,
    val selectedCourseIds: List<String>
)