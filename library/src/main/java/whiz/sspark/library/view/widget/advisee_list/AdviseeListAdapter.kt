package whiz.sspark.library.view.widget.advisee_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.databinding.ViewAdviseeProfileItemViewBinding
import whiz.sspark.library.databinding.ViewCenterTextBinding
import whiz.sspark.library.view.widget.base.CenterTextViewHolder
import java.lang.Exception

class AdviseeListAdapter: ListAdapter<AdviseeListAdapter.AdviseeListItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val ADVISEE_ITEM_VIEW = 3333
        const val NO_ADVISEE_ITEM_VIEW = 4444
    }

    private var recyclerViewHeight = 0

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item ) {
            is AdviseeListItem.Student -> ADVISEE_ITEM_VIEW
            is AdviseeListItem.NoAdvisee -> NO_ADVISEE_ITEM_VIEW
        }
    }

    fun notifyOnSizeChange(height: Int) {
        try {
            recyclerViewHeight = height
            val isNoAdviseeTitle = getItem(0) is AdviseeListItem.NoAdvisee
            if (isNoAdviseeTitle) {
                notifyItemChanged(0)
            }
        } catch (e: Exception) { }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        recyclerViewHeight = parent.height

        return if (viewType == ADVISEE_ITEM_VIEW) {
            AdviseeProfileViewHolder(ViewAdviseeProfileItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            CenterTextViewHolder(ViewCenterTextBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (item) {
            is AdviseeListItem.Student -> (holder as AdviseeProfileViewHolder).init(item.advisee)
            is AdviseeListItem.NoAdvisee -> (holder as CenterTextViewHolder).apply {
                init(item.title)

                itemView.post {
                    val lastItemBottom = itemView.bottom
                    val lastItemHeight = itemView.height
                    val heightDifference = recyclerViewHeight - lastItemBottom
                    val requiredHeight = lastItemHeight + heightDifference

                    if (requiredHeight != itemView.height) {
                        holder.itemView.layoutParams.height = requiredHeight
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    sealed class AdviseeListItem {
        data class Student(val advisee: Advisee) : AdviseeListItem()
        data class NoAdvisee(val title: String) : AdviseeListItem()
    }

    private class DiffCallback : DiffUtil.ItemCallback<AdviseeListItem>() {
        override fun areItemsTheSame(oldItem: AdviseeListItem, newItem: AdviseeListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AdviseeListItem, newItem: AdviseeListItem): Boolean {
            return oldItem == newItem
        }
    }
}