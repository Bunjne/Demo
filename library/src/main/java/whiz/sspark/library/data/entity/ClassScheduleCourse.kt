package whiz.sspark.library.data.entity

data class ClassScheduleCourse(
        val startTime: String,
        val endTime: String,
        val code: String,
        val name: String,
        val color: Int,
        val room: String,
        val instructorNames: List<String>,
)