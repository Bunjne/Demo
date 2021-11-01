package whiz.sspark.library

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import okhttp3.Interceptor
import okhttp3.Response
import whiz.sspark.library.data.enum.ProjectType

object SSparkLibrary {
    lateinit var onSessionExpired: () -> Unit
    lateinit var baseInterceptor: (chain: Interceptor.Chain) -> Response
    lateinit var boldTypeface: Typeface
    lateinit var boldSerifTypeface: Typeface
    lateinit var regularTypeface: Typeface
    lateinit var regularSerifTypeface: Typeface

    var projectType: ProjectType? = null
    var apiKey = ""
    var clientId = ""
    var clientSecret = ""
    var baseUrl = ""
    var collaborationSocketBaseURL = ""

    var isDarkModeEnabled = false
    var isAutoDarkModeEnabled = false
    var isSystemDarkModeEnable = false
    var isChangeDarkModeFromSetting = false

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

    fun setOnSessionExpireCallback(onSessionExpired: () -> Unit) {
        this.onSessionExpired = onSessionExpired
    }

    fun setInterceptor(baseInterceptor: (chain: Interceptor.Chain) -> Response) {
        this.baseInterceptor = baseInterceptor
    }
}