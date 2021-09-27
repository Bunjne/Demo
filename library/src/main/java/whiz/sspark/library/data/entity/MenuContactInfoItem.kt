package whiz.sspark.library.data.entity

data class MenuContactInfoItem(
    val imageUrl: String,
    val gender: String,
    val name: String,
    val description: String,
    val contactInfoItems: List<MenuContactItem> = listOf()
)