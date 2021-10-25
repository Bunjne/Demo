package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.PreviewMessageItem
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewMenuPreviewMessageWidgetBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show

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
            binding.ivNotificationCountGradientBackground.show(R.drawable.bg_primary_oval_gradient_0)
            binding.tvLastUpdate.text = date?.convertToDateString(
                defaultPattern = DateTimePattern.todayAbbreviatedDateFormatEn,
                dayMonthThPattern = DateTimePattern.todayAbbreviatedDayMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            )

            if (notificationCount > 0) {
                binding.tvNotificationCount.visibility = View.VISIBLE
                binding.ivNotificationCountGradientBackground.visibility = View.VISIBLE
                binding.tvNotificationCount.text = notificationCount.toString()
            } else {
                binding.tvNotificationCount.visibility = View.GONE
                binding.ivNotificationCountGradientBackground.visibility = View.GONE
            }
        }
    }
}