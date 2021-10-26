package whiz.sspark.library.view.screen.event.event_registered

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewEventRegisteredActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.widget.event.EventRegisteredAdapter

class EventRegisteredActivityView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewEventRegisteredActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(events: List<EventRegisteredAdapter.EventRegisteredAdapterViewType>,
             segmentTitles: List<String>,
             title: String,
             onTabClicked: (Int) -> Unit,
             onRefresh: () -> Unit,
             onEventClicked: (String, String) -> Unit) {
        binding.srlEvent.setOnRefreshListener {
            onRefresh()
        }
        binding.tvTitle.text = title

        binding.vSegment.init(segmentTitles, onTabClicked)

        with(binding.rvEventRegistered) {
            layoutManager = LinearLayoutManager(context)

            adapter = EventRegisteredAdapter(
                context = context,
                items = events,
                onEventClicked = onEventClicked
            )
        }
    }

    fun renderEvents(events: MutableList<EventRegisteredAdapter.EventRegisteredAdapterViewType>, updatedEvent: List<EventRegisteredAdapter.EventRegisteredAdapterViewType>) {
        binding.rvEventRegistered.adapter?.updateItem(events, updatedEvent)

        if (updatedEvent.isNullOrEmpty()) {
            binding.tvNoEventRegistered.visibility = View.VISIBLE
            binding.rvEventRegistered.visibility = View.GONE
        } else {
            binding.rvEventRegistered.visibility = View.VISIBLE
            binding.tvNoEventRegistered.visibility = View.GONE
        }
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun setSwipeRefreshLoading(isLoading: Boolean) {
        binding.srlEvent.isRefreshing = isLoading
    }

    fun setSelectedTab(tab: Int) {
        binding.vSegment.selectTab(tab)
    }
}