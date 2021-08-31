package whiz.sspark.library.view.today.happenings.widget.news.news_small

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.databinding.ViewHappeningsNewsSmallTextOnlyBinding
import whiz.sspark.library.extension.toLocalDate
import whiz.sspark.library.extension.toTodayAbbreviatedDateFormat

class HappeningsNewsSmallTextOnlyView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHappeningsNewsSmallTextOnlyBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(newsDetail: NewsDetail) {
        with(newsDetail) {
            binding.tvNewsSmallTextOnlyTitle.text = title
            binding.tvNewsSmallTextOnlyAuthor.text = publisher.name.toUpperCase()
            binding.tvNewsSmallTextOnlyDate.text = startedAt.toLocalDate()!!.toTodayAbbreviatedDateFormat().toUpperCase()
        }
    }
}