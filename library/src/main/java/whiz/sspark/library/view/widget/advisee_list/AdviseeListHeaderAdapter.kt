package whiz.sspark.library.view.widget.advisee_list

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.databinding.ViewSearchItemViewBinding
import whiz.sspark.library.databinding.ViewSegmentItemViewBinding
import whiz.sspark.library.view.widget.base.SearchViewHolder
import whiz.sspark.library.view.widget.base.SegmentViewHolder

class AdviseeListHeaderAdapter(private val segmentTitles: List<String>,
                               private val onSegmentClicked: (Int) -> Unit,
                               private val onTextChanged: (String) -> Unit): ListAdapter<AdviseeListHeaderAdapter.AdviseeListHeaderItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val SEARCH_VIEW_TYPE = 1111
        const val SEGMENT_VIEW_TYPE = 2222
    }

    private var selectedTab = 0
    private var currentText = ""

    fun setInitialAdapter(selectedTab: Int, text: String) {
        this.currentText = text
        this.selectedTab = selectedTab
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            currentText = s.toString()
            onTextChanged(currentText)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when(item) {
            is AdviseeListHeaderItem.Search -> SEARCH_VIEW_TYPE
            is AdviseeListHeaderItem.Segment -> SEGMENT_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SEGMENT_VIEW_TYPE -> SegmentViewHolder(ViewSegmentItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> SearchViewHolder(ViewSearchItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(item) {
            is AdviseeListHeaderItem.Segment -> (holder as SegmentViewHolder).init(
                segments = segmentTitles,
                onTabClicked = {
                    selectedTab = it
                    onSegmentClicked(it)
                },
                initialTab = selectedTab
            )

            is AdviseeListHeaderItem.Search -> (holder as SearchViewHolder).apply {
                setText(currentText)
                init(textWatcher)
            }
        }
    }

    sealed class AdviseeListHeaderItem {
        object Search : AdviseeListHeaderItem()
        object Segment : AdviseeListHeaderItem()
    }

    private class DiffCallback : DiffUtil.ItemCallback<AdviseeListHeaderItem>() {
        override fun areItemsTheSame(oldItem: AdviseeListHeaderItem, newItem: AdviseeListHeaderItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AdviseeListHeaderItem, newItem: AdviseeListHeaderItem): Boolean {
            return oldItem == newItem
        }
    }
}