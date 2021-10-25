package whiz.sspark.library.data.entity

import java.util.*

data class WeekSelection(
        val isShowNextPageButton: Boolean,
        val isShowPreviousPageButton: Boolean,
        val startDate: Date,
        val endDate: Date
)