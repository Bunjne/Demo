package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName

data class ScheduleSlot(
        val dayNumber: Int = 0,
        val startTime: String = "",
        val endTime: String = "",
        var color: String = ""
)