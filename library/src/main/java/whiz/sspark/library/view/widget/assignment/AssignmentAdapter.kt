package whiz.sspark.library.view.widget.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.databinding.ViewAssignmentPreviewItemBinding

class AssignmentAdapter: PagingDataAdapter<Assignment, AssignmentPreviewViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentPreviewViewHolder {
        return AssignmentPreviewViewHolder(ViewAssignmentPreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, true))
    }

    override fun onBindViewHolder(holder: AssignmentPreviewViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.init(holder.itemView.context, it)
        }
    }

    private class DiffCallBack: DiffUtil.ItemCallback<Assignment>() {
        override fun areItemsTheSame(oldItem: Assignment, newItem: Assignment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Assignment, newItem: Assignment): Boolean {
            return oldItem == newItem
        }
    }
}