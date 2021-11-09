package whiz.sspark.library.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Attachment
import java.io.File

fun openFile(context: Context, attachment: Attachment) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(attachment.url))
    context.startActivity(Intent.createChooser(intent, context.resources.getString(R.string.class_post_open_file_title)))
}

fun getFilePickerIntent(): Intent {
    val mimeTypes = arrayOf("application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
        "text/plain",
        "application/pdf",
        "application/zip")

    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "*/*"
        putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    }

    return intent
}

fun getMimeType(it: File) = MimeTypeMap
    .getSingleton()
    .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(it.path))

fun deleteFileDirectory(fileOrDirectory: File) {
    if (fileOrDirectory.isDirectory) {
        fileOrDirectory.listFiles()?.forEach {
            deleteFileDirectory(it)
        }
    }

    fileOrDirectory.delete()
}