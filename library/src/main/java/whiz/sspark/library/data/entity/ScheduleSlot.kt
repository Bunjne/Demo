package whiz.sspark.library.data.entity

data class ScheduleSlot(
        val courseCode: String,
        val dayNumber: Int,
        val startTime: String,
        val endTime: String,
        var color: String
)