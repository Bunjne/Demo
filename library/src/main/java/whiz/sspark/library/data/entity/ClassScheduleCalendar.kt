package whiz.sspark.library.data.entity

import java.util.*

data class ClassScheduleCalendar(
        val slots: List<ScheduleSlot>,
        val scheduleTimes: List<String>,
        val dates: List<Date>
)