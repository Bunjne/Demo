package whiz.sspark.library.view.today

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import whiz.sspark.library.data.entity.TimelineItem
import whiz.sspark.library.data.entity.TimelineResponse
import whiz.sspark.library.data.enum.TimeLineBodyFontStyle
import whiz.sspark.library.databinding.ViewTodayTimelineBinding
import whiz.sspark.library.utility.getDifferenceDayTimelineValue
import whiz.sspark.library.view.today.timeline.TimelineAdapter
import whiz.sspark.library.view.today.timeline.TimelineSegmentAdapter
import whiz.sspark.library.view.today.timeline.TimelineUniversityAnnouncementsAdapter
import java.util.*
import kotlin.math.abs

class TodayTimelineView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTodayTimelineBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var timelineItems: MutableList<TimelineItem> = mutableListOf()
    private var segmentItems: MutableList<TimelineSegmentAdapter.Item> = mutableListOf()
    private var universityEventItems: MutableList<String> = mutableListOf()

    private var segmentAdapter: TimelineSegmentAdapter?  = null

    private var currentSegmentPosition  = -1
    private var currentUniversityEvent  = 0
    private val countdownMillisecond = 5 * 1000L
    private val stepperMillisecond = 1 * 1000L
    private var timer: CountDownTimer? = null

    fun init(onRefresh: () -> Unit,
             onItemClicked: (String) -> Unit,
             onSegmentSelected: (Date, Int) -> Unit) {
        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }

        timelineItems.clear()
        universityEventItems.clear()

        segmentAdapter = TimelineSegmentAdapter(
            context = context,
            items = segmentItems) { date, differDay, position ->
            if (!binding.srlContainer.isRefreshing && position != currentSegmentPosition) {
                currentSegmentPosition = position
                onSegmentSelected(date, differDay)
                segmentAdapter?.setPosition(position)
            }
        }

        with(binding.rvSegment) {
            layoutManager = LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false)
            adapter = segmentAdapter
        }

        with(binding.vpEvent) {
            isUserInputEnabled = false
            setPageTransformer(FadePageTransformer())
            adapter = TimelineUniversityAnnouncementsAdapter(context = context,
                items = universityEventItems)
        }

        with(binding.rvSchedule) {
            layoutManager = LinearLayoutManager(context)
            adapter = TimelineAdapter(context = context,
                items = timelineItems,
                onItemClicked = onItemClicked)
        }

        timer = object : CountDownTimer(countdownMillisecond, stepperMillisecond) {
            override fun onFinish() {
                if (currentUniversityEvent >= universityEventItems.size - 1) {
                    currentUniversityEvent = 0
                } else {
                    currentUniversityEvent += 1
                }

                binding.vpEvent.currentItem = currentUniversityEvent
                timer?.cancel()
                timer?.start()
            }

            override fun onTick(milliSecond: Long) {

            }
        }
    }

    fun updateTimeline(timelineResponse: TimelineResponse) {
        if (timelineResponse.item.isNotEmpty()) {
            binding.rvSchedule.visibility = View.VISIBLE
            binding.tvNothing.visibility = View.GONE

            val filteredTimelineBody = timelineResponse.item.filterNot { it.body.any { it.text.isNullOrBlank() && it.style == TimeLineBodyFontStyle.QR.style } }.sortedBy { it.order }
            with(timelineItems) {
                clear()
                addAll(filteredTimelineBody)
            }

            binding.rvSchedule.adapter?.notifyDataSetChanged()
        } else {
            renderNothingView()
        }
    }

    fun updateSegment(today: Date) {
        segmentItems.clear()

        for (i in -3..6) {
            val date = Calendar.getInstance().apply {
                time = today
                add(Calendar.DATE, i)
            }.time
            segmentItems.add(
                TimelineSegmentAdapter.Item(
                date = date,
                differenceDay = getDifferenceDayTimelineValue(date, today)
            ))
        }

        with (binding.rvSegment) {
            adapter?.notifyDataSetChanged()
            (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(3, 0);
        }

        setSelectedSegment(3)
    }

    fun renderNothingView() {
        binding.tvNothing.visibility = View.VISIBLE
        binding.rvSchedule.visibility = View.GONE
    }

    fun updateUniversityEvent(segments: List<String>) {
        universityEventItems.clear()
        universityEventItems.addAll(segments)
        binding.vpEvent.adapter?.notifyDataSetChanged()
    }

    fun setSwipeRefreshLoading(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading
    }

    fun setSelectedSegment(position: Int): Date? {
        return segmentItems.getOrNull(position)?.apply {
            currentSegmentPosition = position
            segmentAdapter?.setPosition(position)
        }?.date
    }

    fun pauseTimer() {
        timer?.cancel()
    }

    fun resumeTimer() {
        timer?.start()
    }
}

class FadePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = page.width * -position
        if (position <= -1.0f || position >= 1.0f) {
            page.alpha = 0.0f
        } else if (position == 0.0f) {
            page.alpha = 1.0f
        } else {
            page.alpha = 1.0f - abs(position)
        }
    }
}