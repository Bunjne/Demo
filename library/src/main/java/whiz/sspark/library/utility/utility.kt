package whiz.sspark.library.utility

fun isPrimaryHighSchool(academicGrade: Int): Boolean {
    return academicGrade in 7..9
}

fun getHighSchoolLevel(academicGrade: Int?): Int {
    return if (academicGrade != null && academicGrade in 7..12) {
        academicGrade - 6
    } else {
        1
    }
}