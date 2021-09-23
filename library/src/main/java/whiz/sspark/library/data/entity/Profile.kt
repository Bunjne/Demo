package whiz.sspark.library.data.entity

data class Profile(
    val imageUrl: String?,
    val fullName: String?,
    val gender: String?,
    val code: String?,
    val position: String? = null,
    val firstName: String?,
    val middleName: String?,
    val lastName: String?
)