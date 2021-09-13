package whiz.sspark.library.utility

import android.content.Context
import android.content.res.Configuration

fun isDarkModeEnabled(context: Context) = context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES