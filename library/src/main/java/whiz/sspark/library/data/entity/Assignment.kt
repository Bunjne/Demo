package whiz.sspark.library.data.entity

import java.util.*

data class Assignment(
    val color: String,
    val courseName: String,
    val courseCode: String,
    val createdAt: Date,
    val updatedAt: Date,
    val deadlineAt: Date,
    val title: String,
    val description: String,
    val instructorName: String,
    val imageUrl: String,
    val gender: String,
    val attachments: List<Attachment>
)