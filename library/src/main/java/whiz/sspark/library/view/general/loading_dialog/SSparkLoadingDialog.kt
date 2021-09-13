package whiz.sspark.library.view.general.loading_dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import whiz.sspark.library.databinding.ViewLoadingDialogBinding

class SSparkLoadingDialog(context: Context) {

    private val dialog: AlertDialog = AlertDialog.Builder(context)
        .setView(ViewLoadingDialogBinding.inflate(LayoutInflater.from(context)).root)
        .setCancelable(false)
        .create()

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}