package whiz.sspark.library.utility

fun getHighSchoolLevel(academicGrade: Int?): Int {
    return if (academicGrade != null && academicGrade in 7..12) {
        academicGrade - 6
    } else {
        1
    }
}