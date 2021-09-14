package whiz.sspark.library.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Attachment

fun openFile(context: Context, attachment: Attachment) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(attachment.url))
    context.startActivity(Intent.createChooser(intent, context.resources.getString(R.string.class_post_open_file_title)))
}