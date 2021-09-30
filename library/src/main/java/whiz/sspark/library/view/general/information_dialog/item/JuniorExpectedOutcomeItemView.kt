package whiz.sspark.library.view.general.information_dialog.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import whiz.sspark.library.databinding.ViewJuniorExpectedResultDialogItemBinding

class JuniorExpectedOutcomeItemView: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewJuniorExpectedResultDialogItemBinding by lazy {
        ViewJuniorExpectedResultDialogItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(number: Int,
             description: String) {
        binding.tvNumber.text = number.toString()
        binding.tvExpectedOutcomeText.text = description
    }
}