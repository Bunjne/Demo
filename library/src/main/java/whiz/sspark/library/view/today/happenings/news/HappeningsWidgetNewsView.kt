package whiz.sspark.library.view.today.happenings.news

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.HighlightHeader
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.databinding.ViewHappeningsWidgetNewsBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.view.today.happenings.news.news_small.HappeningsNewsSmallAdapter

class HappeningsWidgetNewsView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHappeningsWidgetNewsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(header: HighlightHeader,
             news: List<NewsDetail>,
             latestPosition: Int,
             onNewsClicked: (NewsDetail) -> Unit,
             onSeeMoreClicked: (HighlightHeader) -> Unit,
             onScrolled: (Int) -> Unit) {
        binding.tvTitle.text = header.title
        binding.ivMore.show(R.drawable.ic_arrow_right)

        setOnClickListener {
            onSeeMoreClicked(header)
        }

        with(binding.rvSmallNews) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HappeningsNewsSmallAdapter(context, news, onNewsClicked)

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