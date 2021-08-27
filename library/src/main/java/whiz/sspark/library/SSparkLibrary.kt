package whiz.sspark.library

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import whiz.sspark.library.data.enum.ProjectType

object SSparkLibrary {
    lateinit var boldTypeface: Typeface
    lateinit var boldSerifTypeface: Typeface
    lateinit var regularTypeface: Typeface
    lateinit var regularSerifTypeface: Typeface

    var projectType: ProjectType? = null
    var baseUrl = "https://auusparkapi-stg.azurewebsites.net/api/"

    fun setProjectType(context: Context, type: ProjectType) {
        this.projectType = type
        when(type) {
            ProjectType.TSS -> {
                boldTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_semi_bold) ?: Typeface.DEFAULT_BOLD
                boldSerifTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_semi_bold) ?: Typeface.DEFAULT_BOLD
                regularTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_regular) ?: Typeface.DEFAULT
                regularSerifTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_regular) ?: Typeface.DEFAULT
            }
            else -> {
                boldTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_semi_bold) ?: Typeface.DEFAULT_BOLD
                boldSerifTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_semi_bold) ?: Typeface.DEFAULT_BOLD
                regularTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_regular) ?: Typeface.DEFAULT
                regularSerifTypeface = ResourcesCompat.getFont(context, R.font.noto_sans_thai_regular) ?: Typeface.DEFAULT
            }
        }
    }
}