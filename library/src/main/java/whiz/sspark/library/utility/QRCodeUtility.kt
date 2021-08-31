package whiz.sspark.library.utility

import android.graphics.Bitmap
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.color.Color
import com.github.sumimakito.awesomeqr.option.logo.Logo

fun generateSimpleQRCode(content: String, width: Int? = null, overlayLogo: Bitmap? = null) = AwesomeQrRenderer.render(RenderOption().apply {
        color = Color().apply {
                light = android.graphics.Color.WHITE
                dark = android.graphics.Color.BLACK
                background = android.graphics.Color.WHITE
                auto = false
        }

        if (overlayLogo != null) {
                logo = Logo(overlayLogo, scale = 0.3f)
        }

        if (width != null) {
           size = width
        }

        borderWidth = 0
        patternScale = 1.0f
        this.content = content
}).bitmap