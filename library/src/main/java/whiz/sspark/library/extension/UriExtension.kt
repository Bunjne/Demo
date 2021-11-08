package whiz.sspark.library.extension

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.util.*

fun Uri.toAccessiblePath(context: Context, path: File?): String {

    var fileName = "uploaded ${Date().time}"
    val cursor = context.contentResolver.query(this, null, null, null, null)
    cursor.use { cursor ->
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }

    val inputStream = context.contentResolver.openInputStream(this)
    val newFile = File(path ?: context.getExternalFilesDir(null), fileName)
    val outputStream = FileOutputStream(newFile)

    val buffer = ByteArray(1024)
    var len = inputStream?.read(buffer) ?: 0

    while(len > 0) {
        outputStream.write(buffer, 0, len)
        len = inputStream?.read(buffer) ?: 0
    }

    outputStream.close()
    inputStream?.close()

    return newFile.path
}