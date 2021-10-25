package whiz.sspark.library.view.screen.event.event_registered

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewEventRegisteredActivityBinding
import whiz.sspark.library.extension.showViewStateX

class EventRegisteredActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewEventRegisteredActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(segmentTitles: List<String>,
             title: String,
             onTabClicked: (Int) -> Unit) {
        binding.tvTitle.text = title

        binding.vSegment.init(segmentTitles, onTabClicked)
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun c() {
        binding.tvTitle.visibility  = View.VISIBLE
    }
}