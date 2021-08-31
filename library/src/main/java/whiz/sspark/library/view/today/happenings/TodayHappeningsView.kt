package whiz.sspark.library.view.today.happenings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Event
import whiz.sspark.library.data.entity.HighlightHeader
import whiz.sspark.library.data.entity.News
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.data.enum.HighlightType
import whiz.sspark.library.databinding.ViewTodayHappeningsBinding
import whiz.sspark.library.view.today.happenings.widget.event.HappeningsWidgetEventAdapter
import whiz.sspark.library.view.today.happenings.widget.news.HappeningsWidgetNewsAdapter
import whiz.sspark.library.view.today.happenings.widget.news_flag.HappeningsWidgetNewsFlagAdapter

class TodayHappeningsView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTodayHappeningsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var concatAdapter: ConcatAdapter = ConcatAdapter()
    private var newsFlagAdapter: HappeningsWidgetNewsFlagAdapter? = null
    private var newsAdapter: HappeningsWidgetNewsAdapter? = null
    private var eventAdapter: HappeningsWidgetEventAdapter? = null

    private val newsFlagList: MutableList<HappeningsWidgetNewsFlagAdapter.Item> = mutableListOf()
    private val newsList: MutableList<HappeningsWidgetNewsAdapter.Item> = mutableListOf()
    private val eventList: MutableList<HappeningsWidgetEventAdapter.Item> = mutableListOf()

    fun init(onRefresh: () -> Unit,
             onSeeMoreClicked: (HighlightHeader) -> Unit,
             onNewsClicked : (NewsDetail) -> Unit,
             onEventClicked: (Event) -> Unit) {
        clearHighlight()

        binding.srlContainer.setOnRefreshListener {
            clearHighlight()
            onRefresh()
        }

        newsFlagAdapter =  HappeningsWidgetNewsFlagAdapter(context, newsFlagList, onNewsClicked)
        newsAdapter = HappeningsWidgetNewsAdapter(context, newsList, onNewsClicked, onSeeMoreClicked)
        eventAdapter = HappeningsWidgetEventAdapter(context, eventList, onEventClicked, onSeeMoreClicked)

        concatAdapter = ConcatAdapter(newsFlagAdapter, newsAdapter, eventAdapter)

        with(binding.rvSchedule) {
            layoutManager = LinearLayoutManager(context)
            adapter = concatAdapter
        }
    }

    fun clearHighlight() {
        val adapterList = mutableListOf(newsFlagAdapter, newsAdapter, eventAdapter)

        listOf(newsFlagList, newsList, eventList).forEachIndexed { index, list ->
            val size = list.size
            list.clear()
            when {
                size == 1 -> adapterList[index]?.notifyItemRemoved(0)
                size > 1 -> adapterList[index]?.notifyItemRangeRemoved(0, size)
            }
        }

        newsFlagAdapter?.setLatestPosition(0)
        eventAdapter?.setLatestPosition(0)
    }

    fun addFlagNewsItem(news: List<NewsDetail>) {
        if (news.isNotEmpty()) {
            newsFlagList.add(HappeningsWidgetNewsFlagAdapter.Item(news, HighlightHeader(type = HighlightType.FLAG_ANNOUNCEMENT.type, title = resources.getString(R.string.today_happenings_announcement))))
            newsFlagAdapter?.notifyItemInserted(0)
        }
    }

    fun addNewsItem(news: List<News>) {
        news.forEach {
            val unFlaggedNews = it.news.filter { !it.isFlagged }
            if (unFlaggedNews.isNotEmpty()) {
                newsList.add(HappeningsWidgetNewsAdapter.Item(unFlaggedNews, HighlightHeader(type = HighlightType.ANNOUNCEMENT.type, title = it.levelName, level = it.level)))
            }
        }

        if (newsList.isNotEmpty()) {
            newsAdapter?.notifyItemRangeInserted(0, newsList.size)
        }
    }

    fun addEventsItem(events: List<Event>) {
        if (events.isNotEmpty()) {
            eventList.add(HappeningsWidgetEventAdapter.Item(events, HighlightHeader(type = HighlightType.EVENT.type, title = resources.getString(R.string.event_list_title))))
            eventAdapter?.notifyItemInserted(0)
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading
    }
}