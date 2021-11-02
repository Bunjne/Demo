package whiz.sspark.library.data.entity

import java.util.*

data class Assignment(
    val id: String,
    val startColor: String,
    val endColor: String,
    val courseTitle: String,
    val classGroupId: String,
    val createdAt: Date,
    val updatedAt: Date,
    val deadlineAt: Date,
    val title: String,
    val description: String,
    val instructorName: String,
    val instructorId: String,
    val imageUrl: String,
    val gender: String,
    val attachments: List<Attachment>
)