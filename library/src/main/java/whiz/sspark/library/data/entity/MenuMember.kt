package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.MenuSegmentType

data class MenuMember(
    val type: MenuSegmentType,
    val imageUrl: String,
    val gender: String,
    val description: String,
    val name: String
)