package whiz.sspark.library.data.entity

data class Advisee(
    val nickname: String,
    val code: String,
    val title: String,
    val name: String,
    val imageUrl: String,
    val gender: String,
    val GPA: Float,
    val credit: Int,
    val maxCredit: Int
)