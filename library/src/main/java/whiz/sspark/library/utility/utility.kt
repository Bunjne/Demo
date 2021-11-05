package whiz.sspark.library.utility

import android.app.Activity
import android.content.Context

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

fun isValidContextForGlide(context: Context?): Boolean {
    if (context == null) {
        return false
    }

    if (context is Activity) {
        if (context.isDestroyed || context.isFinishing) {
            return false
        }
    }

    return true
}