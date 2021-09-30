package whiz.sspark.library.view.general.information_dialog.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import whiz.sspark.library.databinding.ViewSeniorExpectedResultDialogItemBinding

class SeniorExpectedOutcomeItemView: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewSeniorExpectedResultDialogItemBinding by lazy {
        ViewSeniorExpectedResultDialogItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(level: String,
             description: String) {
        binding.tvLevel.text = level
        binding.tvOutcomeDescription.text = description
    }
}