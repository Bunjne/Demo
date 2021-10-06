package whiz.sspark.library.extension

import android.content.Context
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ApiResponseX

fun ApiResponseX.toAlertMessage(context: Context): String {
    return if (this.message.isBlank()) {
        this.statusCode.toErrorMessage(context)
    } else {
        this.message
    }
}

fun Int.toErrorMessage(context: Context) = when (this) {
    400 -> context.resources.getString(R.string.general_alertmessage_serviceerror_400_title)
    401 -> context.resources.getString(R.string.general_alertmessage_serviceerror_401_title)
    403 -> context.resources.getString(R.string.general_alertmessage_serviceerror_403_title)
    404 -> context.resources.getString(R.string.general_alertmessage_serviceerror_404_title)
    500 -> context.resources.getString(R.string.general_alertmessage_serviceerror_500_title)
    503 -> context.resources.getString(R.string.general_alertmessage_serviceerror_503_title)
    else -> context.resources.getString(R.string.general_alertmessage_somethingwentwrong_title)
}