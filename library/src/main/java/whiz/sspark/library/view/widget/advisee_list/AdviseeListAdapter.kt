package whiz.sspark.library.view.widget.advisee_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.databinding.ViewAdviseeProfileItemViewBinding

class AdviseeListAdapter: ListAdapter<Advisee, AdviseeProfileViewHolder>(DiffCallback()) {

    companion object {
        const val ADVISEE_ITEM_VIEW = 3333
    }

    override fun getItemViewType(position: Int): Int {
        return ADVISEE_ITEM_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviseeProfileViewHolder {
        return AdviseeProfileViewHolder(ViewAdviseeProfileItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, true))
    }

    override fun onBindViewHolder(holder: AdviseeProfileViewHolder, position: Int) {
        val item = getItem(position)
        holder.init(item)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Advisee>() {
        override fun areItemsTheSame(oldItem: Advisee, newItem: Advisee): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Advisee, newItem: Advisee): Boolean {
            return oldItem == newItem
        }
    }
}