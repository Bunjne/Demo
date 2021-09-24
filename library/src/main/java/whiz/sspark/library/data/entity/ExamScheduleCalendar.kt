package whiz.sspark.library.data.entity

data class ExamScheduleCalendar(
        val month: Int,
        val year: Int,
        val entries: List<CalendarEntry>,
        val isExamCalendar: Boolean
)