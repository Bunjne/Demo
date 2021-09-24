package whiz.sspark.library.data.entity

data class MenuContactInfoItem(
    val imageUrl: String,
    val gender: String,
    val name: String,
    val description: String,
    val officePhone: String = "",
    val personalPhone: String = "",
    val officeEmail: String = "",
    val personalEmail: String = "",
    val contactInfoItems: List<MenuContactItem> = listOf()
)