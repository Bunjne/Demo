package whiz.sspark.library.utility

fun getHighSchoolLevel(academicGrade: Int?): Int {
    return if (academicGrade == null) {
        1
    } else {
        academicGrade - 6
    }
}