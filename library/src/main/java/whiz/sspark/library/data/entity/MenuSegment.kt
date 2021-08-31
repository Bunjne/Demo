package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.MenuSegmentType

data class MenuSegment(
    val title: String,
    val type: MenuSegmentType
)