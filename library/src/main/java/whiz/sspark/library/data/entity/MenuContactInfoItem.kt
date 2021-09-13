package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.MenuSegmentType

data class MenuContactInfoItem(
    val menuSegmentType: MenuSegmentType,
    val index: Int,
    val imageUrl: String,
    val gender: String,
    val name: String,
    val description: String,
    val officePhone: String = "",
    val personalPhone: String = "",
    val officeEmail: String = "",
    val personalEmail: String = ""
)