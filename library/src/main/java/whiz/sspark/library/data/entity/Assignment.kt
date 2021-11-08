package whiz.sspark.library.data.entity

import whiz.sspark.library.extension.toLocalDate
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
) {
    fun convertToCreateAssignment() = CreateAssignment(
        deadlineAt = deadlineAt.toLocalDate(),
        title = title,
        description = description,
        attachments = attachments.toMutableList()
    )
}

data class CreateAssignment(
    var deadlineAt: Date? = null,
    val title: String = "",
    val description: String = "",
    val attachments: MutableList<Attachment> = mutableListOf()
)