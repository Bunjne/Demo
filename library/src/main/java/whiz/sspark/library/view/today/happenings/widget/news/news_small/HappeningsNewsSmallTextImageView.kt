package whiz.sspark.library.view.today.happenings.widget.news.news_small

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.databinding.ViewHappeningsNewsSmallTextImageBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toLocalDate
import whiz.sspark.library.extension.toTodayAbbreviatedDateFormat

class HappeningsNewsSmallTextImageView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHappeningsNewsSmallTextImageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(newsDetail: NewsDetail) {

        with(newsDetail) {
            binding.ivNewsSmallTextImageView.show(coverImage)
            binding.tvNewsSmallTextImageTitle.text = title
            binding.tvNewsSmallTextImageAuthor.text = publisher.name.capitalize()
            binding.tvNewsSmallTextImageDate.text = startedAt.toLocalDate()!!.toTodayAbbreviatedDateFormat().toUpperCase()
        }
    }
}