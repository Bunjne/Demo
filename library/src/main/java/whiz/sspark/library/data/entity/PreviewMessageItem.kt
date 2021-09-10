package whiz.sspark.library.data.entity

import java.util.*

data class PreviewMessageItem(
    val title: String = "",
    val description: String = "",
    val notificationCount: Int = 0,
    val date: Date? = null
)