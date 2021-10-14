package whiz.sspark.library.view.screen.event.event_list

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.databinding.ViewEventListActivityBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.widget.event.EventListAdapter
import whiz.sspark.library.view.widget.event.EventListHighlightEventContainerAdapter
import java.lang.Exception

class EventListActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: ViewEventListActivityBinding by lazy {
        ViewEventListActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var highlightEventAdapter: EventListHighlightEventContainerAdapter? = null
    private var eventAdapter: EventListAdapter? = null

    fun init(title: String,
             onHistoryClicked: () -> Unit,
             onEventClicked: (String, String) -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTitle.text = title

        with(binding.ivHistory) {
            show(R.drawable.ic_ticket)
            setOnClickListener {
                onHistoryClicked()
            }
        }

        highlightEventAdapter = EventListHighlightEventContainerAdapter(onEventClicked)
        eventAdapter = EventListAdapter(onEventClicked)

        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()

        val concatAdapter = ConcatAdapter(config, highlightEventAdapter, eventAdapter)

        with(binding.rvEvent) {
            layoutManager = LinearLayoutManager(context)
            adapter = concatAdapter
        }

        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateHighlightEventAdapter(events: List<EventList>) {
        highlightEventAdapter?.submitList(listOf(events)) {
            try {
                binding.rvEvent.scrollToPosition(0)
            } catch (e: Exception) { }
        }

        fetchEvent()
    }

    fun updateEventAdapter(events: List<EventList>) {
        eventAdapter?.submitList(events) {
            try {
                binding.rvEvent.scrollToPosition(0)
            } catch (e: Exception) { }
        }

        fetchEvent()
    }

    private fun fetchEvent() {
        val isHasEvent = !eventAdapter?.currentList.isNullOrEmpty()
        val isHasHighlightEvent = !highlightEventAdapter?.currentList.isNullOrEmpty()

        if (isHasEvent || isHasHighlightEvent) {
            binding.tvEvent.visibility = View.GONE
            binding.rvEvent.visibility = View.VISIBLE
        } else {
            binding.tvEvent.visibility = View.VISIBLE
            binding.rvEvent.visibility = View.GONE
        }
    }
}