package whiz.sspark.library.view.today.happenings.widget.news_flag

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import androidx.viewpager2.widget.ViewPager2
import whiz.sspark.library.data.entity.HighlightHeader
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.databinding.ViewHappeningsWidgetNewsFlagBinding

class HappeningsWidgetNewsFlagView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewHappeningsWidgetNewsFlagBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var currentPosition = 0

    private val countdownMillisecond = 5 * 1000L
    private val stepperMillisecond = 1 * 1000L

    private var timer: CountDownTimer? = null

    fun init(header: HighlightHeader,
             news: List<NewsDetail>,
             latestPosition: Int,
             onNewsClicked: (NewsDetail) -> Unit,
             onScrolled: (Int) -> Unit) {
        binding.tvTitle.text = header.title

        timer?.cancel()
        currentPosition = latestPosition

        if (news.size > 1) {
            binding.newsIndicator.init(news.size)
            binding.newsIndicator.visibility = View.VISIBLE

            timer = object : CountDownTimer(countdownMillisecond, stepperMillisecond) {
                override fun onFinish() {
                    if (news.size > 1) {
                        if (currentPosition >= news.size - 1) {
                            currentPosition = 0
                        } else {
                            currentPosition += 1
                        }
                        binding.vpNews.setCurrentItem(currentPosition, true)
                    }
                }

                override fun onTick(milliSecond: Long) {

                }
            }

            binding.vpNews.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    timer?.cancel()
                    timer?.start()

                    currentPosition = position
                    binding.newsIndicator.onPageSelected(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    binding.newsIndicator.onPageScrollStateChanged(state)
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    binding.newsIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    onScrolled(position)
                }
            })
        } else {
            binding.newsIndicator.visibility = View.GONE
        }

        with(binding.vpNews) {
            adapter = HappeningsNewsFlagAdapter(
                context,
                news.sortedByDescending { it.startedAt }.sortedByDescending { it.level },
                onNewsClicked
            )

            doOnPreDraw {
                setCurrentItem(latestPosition, true)
            }

            timer?.start()
        }
    }

    override fun onDetachedFromWindow() {
        timer?.cancel()

        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        timer?.cancel()
        timer?.start()

        super.onAttachedToWindow()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            timer?.cancel()
            timer?.start()
        } else {
            timer?.cancel()
        }
    }
}