package whiz.sspark.library.view.general.image_view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import whiz.sspark.library.R

class TopCropImageView : AppCompatImageView {
    constructor(context: Context) : super(context)  { setupStyleable(context, null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)  { setupStyleable(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)  { setupStyleable(context, attrs) }

    companion object {
        private const val DEFAULT_IMAGE_SCALE: Float = 0.523F
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        recomputeImageMatrix ()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        recomputeImageMatrix ()
        return super.setFrame(l, t, r, b)
    }

    private fun setupStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopCropImageView)
        imageScale = typedArray.getFloat(R.styleable.TopCropImageView_imageScale, DEFAULT_IMAGE_SCALE)
        typedArray.recycle()
    }

    private var imageScale: Float = DEFAULT_IMAGE_SCALE

    init {
        scaleType = ScaleType.MATRIX
    }

    private fun recomputeImageMatrix () {
        val drawable = drawable ?: return
        val matrix = imageMatrix
        val viewWidth = width - paddingLeft - paddingRight
        val viewHeight = height - paddingTop - paddingBottom
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight

        val scale = if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            viewHeight.toFloat() / drawableHeight.toFloat()
        } else {
            viewWidth.toFloat() / drawableWidth.toFloat()
        }

        matrix.setScale(scale, scale)
        if (drawableWidth * scale > viewWidth) {
            val tr = -((drawableWidth * scale - viewWidth) / 2)
            matrix.postTranslate(tr, 0f)
        }
        imageMatrix = matrix
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val defaultHeight = (width * imageScale).toInt()

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(heightSize, defaultHeight)
            MeasureSpec.UNSPECIFIED -> defaultHeight
            else -> defaultHeight
        }

        setMeasuredDimension(width, height)
    }
}