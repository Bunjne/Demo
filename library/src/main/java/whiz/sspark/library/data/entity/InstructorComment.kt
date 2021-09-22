package whiz.sspark.library.data.entity

import java.util.*

data class InstructorComment(
    val name: String,
    val createdAt: Date,
    val comment: String,
    val gender: String,
    val imageUrl: String
)
