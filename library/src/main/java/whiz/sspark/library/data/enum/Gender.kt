package whiz.sspark.library.data.enum

enum class Gender(val type: Long) {
    NOTSPECIFY(0),
    MALE(1),
    FEMALE(2)
}

fun getGender(gender: String?) = when (gender) {
    "1" ,
    "MALE" ,
    "M" -> Gender.MALE

    "2" ,
    "FEMALE" ,
    "F" -> Gender.FEMALE

    else -> Gender.NOTSPECIFY
}