package whiz.sspark.library.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Patterns
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import whiz.sspark.library.utility.isThaiLanguage
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun String.convertToTime(): String {
    return if (this.isBlank()) {
        ""
    } else {
        val timeStamp = this.split(":")
        if (timeStamp.size < 2) {
            ""
        } else {
            "${timeStamp[0]}:${timeStamp[1]}"
        }
    }
}

fun String.toQRCodeDrawable(context: Context, size: Int): Drawable? {
    try {
        val bitMatrix = QRCodeWriter().encode(this, BarcodeFormat.QR_CODE, size, size, mapOf(EncodeHintType.MARGIN to 0))
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val colorPixel = if (bitMatrix.get(x, y)) Color.BLACK else Color.TRANSPARENT
                bitmap.setPixel(x, y, colorPixel)
            }
        }
        return BitmapDrawable(context.resources, bitmap)
    } catch (writerException: WriterException) { }

    return null
}

fun String.getFirstConsonant(): String {
    return if (isThaiLanguage()) {
        val pattern = Pattern.compile("[ก-ฮ]")
        for (i in this) {
            if (pattern.matcher(i.toString()).matches()){
                return i.toString()
            }
        }
         ""
    } else {
        this.trimStart().firstOrNull()?.toString() ?: ""
    }
}

fun String.isUrlValid() = Patterns.WEB_URL.matcher(this).matches()

fun String?.convertToDate(pattern: String) = if (this.isNullOrBlank()) {
    null
} else {
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
}

fun String.isPhoneNumber(): Boolean  {
    if (this.first() != '0') {
        return false
    } else if (this.length == 9 && this.take(2) != "02") {
        return false
    }

    val pattern = Pattern.compile("[0-9]")
    for (number in this) {
        if (!pattern.matcher(number.toString()).matches()) {
            return false
        }
    }

    return true
}

fun String.toPhoneNumber() = StringBuilder().append(this).apply {
    if (!this@toPhoneNumber.isPhoneNumber()) {
        return this.toString()
    } else {
        if (this.take(2) != "02") {
            this.insert(3, '-')
            this.insert(7, '-')
            if (this.length > 12) {
                this.insert(12, '-')
            }
        } else  {
            this.insert(2, '-')
            this.insert(6, '-')
            if (this.length > 11) {
                this.insert(11, '-')
            }
        }
    }
}.toString()