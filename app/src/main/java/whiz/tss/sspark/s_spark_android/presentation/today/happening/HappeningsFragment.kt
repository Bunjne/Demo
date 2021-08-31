package whiz.tss.sspark.s_spark_android.presentation.today.happening

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.data.enum.EventType
import whiz.sspark.library.data.enum.HighlightType
import whiz.sspark.library.data.viewModel.HappeningsViewModel
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentHappeningsBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class HappeningsFragment : BaseFragment() {

    companion object {
        fun newInstance() = HappeningsFragment()
    }

    private val viewModel: HappeningsViewModel by viewModel()

    private var _binding: FragmentHappeningsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHappeningsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeView()
        observeData()
        observeError()
    }

    override fun initView() {
        binding.vHighlight.init(onRefresh = {
            viewModel.getTodayNews()
            viewModel.getEvents(EventType.UPCOMING.type, true)
        }, onSeeMoreClicked =  { header ->
            when (header.type) {
                HighlightType.EVENT.type -> {
//                    activity?.startActivity<EventListActivity>() TODO Waiting for EventList Activity Implementation
                }
                HighlightType.ANNOUNCEMENT.type -> {
//                    startActivval localEvents = localSource.listEvents()
//                if (localEvents != null) {
//                    value = DataWrapperX(
//                            data = localEvents,
//                            error = null,
//                            statusCode = null,
//                            isCacheExisted = true,
//                            latestDateTime = localSource.getEventListLatestDateTime(),
//                            isNetworkPreferred = isNetworkPreferred,
//                            dataSource = DATASOURCE.CACHE
//                    )
//                }ity(Intent(context, NewsListActivity::class.java).apply { TODO Waiting for NewsList Activity Implementation
//                        putExtra("level", header.level)
//                        putExtra("title", header.title)
//                    })
                }
            }
        }, onNewsClicked = {
            if (it.widgetOnClickedUrl.isNotBlank()) {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.widgetOnClickedUrl)))
            } else {
//                startActivity(Intent(context, NewsDetailActivity::class.java).apply {
//                    putExtra("newsId", it.id) TODO Waiting for NewsDetail Activity Implementation
//                })
            }
        }, onEventClicked = { event ->
//            activity?.startActivity<EventDetailActivity>( TODO Waiting for EventDetail Activity Implementation
//                "eventId" to event.id,
//                "viewAsType" to EventViewAsType.ATTENDEE.type
//            )
        })

        viewModel.getTodayNews()
        viewModel.getEvents(EventType.UPCOMING.type, false)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer {
            it?.let {
                binding.vHighlight.setSwipeRefreshLoading(it)
            }
        })
    }

    override fun observeData() {
        viewModel.todayNewsResponse.observe(this, Observer { newsResponses ->
            val flaggedNews = mutableListOf<NewsDetail>()
            newsResponses.forEach {
                flaggedNews.addAll(it.news.filter { it.isFlagged })
            }

            if (flaggedNews.isNotEmpty()) {
                binding.vHighlight.addFlagNewsItem(flaggedNews.toList())
            }

            binding.vHighlight.addNewsItem(newsResponses)
        })

        viewModel.eventsResponse.observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.vHighlight.addEventsItem(it)
            }
        })
    }

    override fun observeError() {

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        binding.vHighlight.clearHighlight()
        viewModelStore.clear()
        super.onConfigurationChanged(newConfig)
    }
}