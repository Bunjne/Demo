package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.GradeSummary
import whiz.sspark.library.databinding.ViewMenuGradeSummaryWidgetBinding

class MenuGradeSummaryWidget: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuGradeSummaryWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             gradeSummarySummary: List<GradeSummary>) {
        binding.tvTitle.text = title
        binding.vGradeSummary.init(
            gradeSummaries = gradeSummarySummary,
            isDrawText = false,
            numberOfCategory = gradeSummarySummary.size)
    }
}