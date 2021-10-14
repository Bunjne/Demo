package whiz.sspark.library.view.widget.event

import android.view.View
import android.widget.AbsListView
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.databinding.ViewEventListHighlightEventContainerItemBinding

class EventListHighlightEventContainerViewHolder(
    private val binding: ViewEventListHighlightEventContainerItemBinding
): RecyclerView.ViewHolder(binding.root) {
    private val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
    private var currentPosition = 0

    fun init(events: List<EventList>,
             latestPosition: Int,
             onScrolled: (Int) -> Unit,
             onEventClicked: (String, String) -> Unit) {
        binding.vIndicator.init(events.size)

        if (latestPosition != -1) {
            currentPosition = latestPosition
            binding.rvEvent.doOnPreDraw {
                binding.rvEvent.scrollToPosition(latestPosition)
            }
        }

        with(binding.rvEvent) {
            layoutManager = this@EventListHighlightEventContainerViewHolder.layoutManager
            adapter = EventListHighlightEventAdapter(
                events,
                onEventClicked
            )
        }

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvEvent)

        binding.rvEvent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    currentPosition = layoutManager.findFirstVisibleItemPosition()
                    binding.vIndicator.onPageSelected(currentPosition)
                    onScrolled(currentPosition)
                }
            }
        })

        if (events.size > 1) {
            binding.vIndicator.visibility = View.VISIBLE
        } else {
            binding.vIndicator.visibility = View.INVISIBLE
        }
    }
}