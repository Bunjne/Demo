package whiz.sspark.library.utility

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.extension.toAlertMessage

fun showApiResponseXAlert(context: Context?, errorResponse: ApiResponseX, okClicked: () -> Unit = {}) {
    if (errorResponse.statusCode in 205..500) {
        context?.let { context ->
            AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.general_alertmessage_title))
                .setCancelable(false)
                .setMessage("${errorResponse.toAlertMessage(context)}\n${context.resources.getString(R.string.general_alertmessage_error_code, errorResponse.code)}")
                .setPositiveButton(R.string.general_text_ok) { _,_ -> okClicked() }
                .show()
        }
    }
}

fun showApiResponseXAlert(context: Context?, errorResponse: ApiResponseX, retryClicked: () -> Unit = {}, cancelClicked: () -> Unit = {}) {
    if (errorResponse.statusCode in 205..500) {
        context?.let { context ->
            AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.general_alertmessage_title))
                .setMessage("${errorResponse.toAlertMessage(context)}\n${context.resources.getString(R.string.general_alertmessage_error_code, errorResponse.code)}")
                .setPositiveButton(R.string.general_text_retry) { _,_ -> retryClicked() }
                .setNegativeButton(R.string.general_text_discard) { _,_ -> cancelClicked() }
                .show()
        }
    }
}

fun Context.showAlertWithOkButton(title: String = "",
                                  message: String = "",
                                  isCancelable: Boolean = true,
                                  onPositiveClicked: (DialogInterface, Int) -> Unit = { _, _ -> },
                                  onDialogDismiss: () -> Unit = { }) {
    val dialog = AlertDialog.Builder(this)
        .setPositiveButton(resources.getString(R.string.general_text_ok)) { dialogInterface, i ->
            onPositiveClicked(dialogInterface, i)
        }
        .setCancelable(isCancelable)
        .setOnDismissListener { onDialogDismiss() }

    if (message.isNotBlank()) {
        dialog.setTitle(title)
        dialog.setMessage(message)
    } else {
        dialog.setMessage(title)
    }

    dialog.show()
}

fun Context.showAlert(title: String,
                      isCancelAble: Boolean = false,
                      onPositiveClicked: () -> Unit = { },
                      positiveTitle: String,
                      onNegativeClicked: () -> Unit = { },
                      negativeTitle: String = "") {
    val dialog = AlertDialog.Builder(this)
        .setMessage(title)
        .setCancelable(isCancelAble)
        .setPositiveButton(positiveTitle) { _, _ ->
            onPositiveClicked()
        }

    if (negativeTitle.isNotBlank()) {
        dialog.setNegativeButton(negativeTitle) { _, _ ->
            onNegativeClicked()
        }
    }

    dialog.show()
}

fun Context.showAlertWithMultipleItems(titles: List<String>,
                                       isCancelAble: Boolean = true,
                                       onItemSelected: (Int) -> Unit) {
    val dialog = AlertDialog.Builder(this)
        .setItems(titles.toTypedArray()) { dialog, index ->
            onItemSelected(index)
        }

    dialog.setCancelable(isCancelAble)
    dialog.show()
}