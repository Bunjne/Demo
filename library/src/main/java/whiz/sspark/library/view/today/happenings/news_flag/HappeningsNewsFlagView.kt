package whiz.sspark.library.view.today.happenings.news_flag

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewHappeningsNewsFlagBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toLocalDate

class HappeningsNewsFlagView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHappeningsNewsFlagBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(newsDetail: NewsDetail,
             onNewsClicked: (NewsDetail) -> Unit) {

        binding.cvContent.setOnClickListener {
            onNewsClicked(newsDetail)
        }

        with(newsDetail) {
            binding.ivBackground.show(coverImage)
            binding.tvTitle.text = title
            binding.tvAuthor.visibility = View.VISIBLE

            binding.tvAuthor.text = newsDetail.publisher.name

            binding.tvDate.text = startedAt.toLocalDate()!!.convertToDateString(
                defaultPattern = DateTimePattern.todayAbbreviatedDateFormatEn,
                dayMonthThPattern = DateTimePattern.todayAbbreviatedDayMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            ).toUpperCase()
        }
    }
}