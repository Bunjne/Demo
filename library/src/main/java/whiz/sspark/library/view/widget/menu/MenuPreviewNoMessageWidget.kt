package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewMenuPreviewNoMessageWidgetBinding

class MenuPreviewNoMessageWidget: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuPreviewNoMessageWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(screen: String) {
        binding.tvScreen.text = screen
    }
}