package whiz.sspark.library.view.widget.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.databinding.ViewAssignmentPreviewItemBinding

class AssignmentAdapter(private val onAssignmentClicked: (Assignment) -> Unit): ListAdapter<Assignment, AssignmentPreviewViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentPreviewViewHolder {
        return AssignmentPreviewViewHolder(ViewAssignmentPreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AssignmentPreviewViewHolder, position: Int) {
        val assignment = getItem(position)
        holder.init(assignment)
        holder.itemView.setOnClickListener {
            onAssignmentClicked(assignment)
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