package whiz.sspark.library.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

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
    val result: BitMatrix? = try {
        MultiFormatWriter().encode(
            this,
            BarcodeFormat.QR_CODE,
            size,
            size,
            mapOf(EncodeHintType.MARGIN to 0)
        )
    } catch (writerException: WriterException) {
        null
    }
    return result?.let {
        val width = result.width
        val height = result.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val colorPixel = if (result.get(x, y)) Color.BLACK else Color.WHITE
                bitmap.setPixel(x, y, colorPixel)
            }
        }
        BitmapDrawable(context.resources, bitmap)
    }
}