package whiz.sspark.library.view.widget.base

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.databinding.ViewSegmentItemViewBinding

class SegmentViewHolder(
    private val binding: ViewSegmentItemViewBinding
): RecyclerView.ViewHolder(binding.root) {
    fun init(segments: List<String>,
             onTabClicked: (Int) -> Unit,
             initialTab: Int) {
        binding.vSegment.init(
            titles = segments,
            onTabClicked = onTabClicked,
            initialTab = initialTab
        )
    }
}