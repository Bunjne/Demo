package whiz.sspark.library.data.entity

import java.util.*

data class Inbox(
    val isRead: Boolean,
    val title: String,
    val detail: String,
    val date: Date
)