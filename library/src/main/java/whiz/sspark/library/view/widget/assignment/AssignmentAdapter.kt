package whiz.sspark.library.view.widget.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.databinding.ViewAssignmentPreviewItemBinding
import whiz.sspark.library.databinding.ViewLoadingBinding
import whiz.sspark.library.view.widget.base.LoadingViewHolder

class AssignmentAdapter(private val onAssignmentClicked: (Assignment) -> Unit): ListAdapter<AssignmentAdapter.AssignmentItem, RecyclerView.ViewHolder>(DiffCallBack()) {

    companion object {
        private const val ASSIGNMENT_VIEW_TYPE = 1111
        private const val LOADING_VIEW_TYPE = 2222
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item) {
            is AssignmentItem.Item -> ASSIGNMENT_VIEW_TYPE
            is AssignmentItem.Loading -> LOADING_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ASSIGNMENT_VIEW_TYPE) {
            AssignmentPreviewViewHolder(ViewAssignmentPreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            LoadingViewHolder(ViewLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (item) {
            is AssignmentItem.Item -> {
                (holder as AssignmentPreviewViewHolder).init(item.assignment)
                holder.itemView.setOnClickListener {
                    onAssignmentClicked(item.assignment)
                }
            }
            is AssignmentItem.Loading -> (holder as LoadingViewHolder).init(item.isShowing)
        }
    }

    sealed class AssignmentItem {
        data class Item(val assignment: Assignment): AssignmentItem()
        data class Loading(var isShowing: Boolean): AssignmentItem()
    }

    private class DiffCallBack: DiffUtil.ItemCallback<AssignmentItem>() {
        override fun areItemsTheSame(oldItem: AssignmentItem, newItem: AssignmentItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AssignmentItem, newItem: AssignmentItem): Boolean {
            return oldItem == newItem
        }
    }
}