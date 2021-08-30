package whiz.sspark.library.view.today.happenings.widget.event

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Event
import whiz.sspark.library.data.entity.HighlightHeader
import whiz.sspark.library.databinding.ViewHappeningsWidgetEventBinding
import whiz.sspark.library.databinding.ViewWidgetEventBinding
import whiz.sspark.library.extension.show

class HappeningsWidgetEventView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHappeningsWidgetEventBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(header: HighlightHeader,
             events: List<Event>,
             latestPosition: Int,
             onEventClicked: (Event) -> Unit,
             onSeeMoreClicked: (HighlightHeader) -> Unit,
             onScrolled: (Int) -> Unit, ) {
        binding.tvTitle.text = header.title
        binding.ivMore.show(R.drawable.ic_arrow_right)

        setOnClickListener {
            onSeeMoreClicked(header)
        }

        with(binding.rvEvent) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HappeningsEventSmallItemAdapter(context, events, onEventClicked)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    onScrolled(recyclerView.computeHorizontalScrollOffset())
                }
            })

            doOnPreDraw {
                scrollBy(latestPosition, 0)
            }
        }
    }
}