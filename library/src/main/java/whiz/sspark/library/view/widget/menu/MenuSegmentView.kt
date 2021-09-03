package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.MenuSegment
import whiz.sspark.library.databinding.ViewMenuSegmentItemBinding

class MenuSegmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuSegmentItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(selectTab: Int,
             segments: List<MenuSegment>,
             onSegmentSelected: (Int) -> Unit) {
        val titles = segments.map { it.title }
        binding.vSegment.init(
            selectTab = selectTab,
            titles = titles,
            onTabClicked = { position ->
                onSegmentSelected(position)
            }
        )
    }
}