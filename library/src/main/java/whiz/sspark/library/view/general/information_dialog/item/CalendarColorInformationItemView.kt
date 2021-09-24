package whiz.sspark.library.view.general.information_dialog.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import whiz.sspark.library.databinding.ViewCalendarColorInformationItemBinding

class CalendarColorInformationItemView: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewCalendarColorInformationItemBinding by lazy {
        ViewCalendarColorInformationItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(color: Int,
             description: String) {
        binding.acvCircleStatus.background_Color = color
        binding.acvCircleStatus.invalidate()
        binding.tvDescription.text = description
    }
}