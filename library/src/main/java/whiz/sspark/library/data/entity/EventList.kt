package whiz.sspark.library.data.entity

import java.util.*

data class EventList(
    val id: String,
    val name: String,
    val startAt: Date,
    val imageUrl: String,
    val location: String
)