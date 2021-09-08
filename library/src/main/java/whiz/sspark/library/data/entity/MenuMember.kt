package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.MenuSegmentType

data class MenuMember(
    val type: MenuSegmentType,
    val index: Int,
    val imageUrl: String,
    val gender: String,
    val description: String,
    val name: String
) {
    fun convertToMemberItem(): MemberItem {
        return MemberItem(
            imageUrl = imageUrl,
            gender = gender,
            description = description,
            name = name
        )
    }
}