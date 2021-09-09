package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.PreviewMessageItem
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewMenuPreviewMessageWidgetBinding
import whiz.sspark.library.extension.convertToDateString

class MenuPreviewMessageWidget: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuPreviewMessageWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             previewMessageInfo: PreviewMessageItem) {
        with(previewMessageInfo) {
            binding.tvScreen.text = title
            binding.tvTitle.text = this.title
            binding.tvDescription.text = description
            binding.tvLastUpdate.text = date?.convertToDateString(
                defaultPattern = DateTimePattern.todayAbbreviatedDateFormatEn,
                dayMonthThPattern = DateTimePattern.todayAbbreviatedDayMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            )
        }
    }
}