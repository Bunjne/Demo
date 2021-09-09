package whiz.sspark.library.view.widget.school_record_activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.ActivityRecordItem
import java.lang.IndexOutOfBoundsException

class JuniorActivityRecordAdapter(private val context: Context): ListAdapter<ActivityRecordItem, RecyclerView.ViewHolder>(JuniorActivityRecordDiffCallback()) {

    companion object {
        private val TITLE_VIEW_TYPE = 1111
        private val STATUS_WITH_DESCRIPTON_VIEW_TYPE = 2222
        private val STATUS_VIEW_TYPE = 3333
        private val TITLE_WITH_DESCRIPTION_VIEW_TYPE = 4444
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        return when {
            item.status != null && item.description != null -> STATUS_WITH_DESCRIPTON_VIEW_TYPE
            item.status != null -> STATUS_VIEW_TYPE
            item.description != null -> TITLE_WITH_DESCRIPTION_VIEW_TYPE
            else -> TITLE_VIEW_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class StatusWithDescriptionViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class StatusViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class TitleWithDescriptionViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    data class Item(
        val title: String? = null,
        val activityRecordItem: ActivityRecordItem? = null
    )

}

private class JuniorActivityRecordDiffCallback : DiffUtil.ItemCallback<JuniorActivityRecordAdapter.Item>() {
    override fun areItemsTheSame(oldItem: JuniorActivityRecordAdapter.Item, newItem: JuniorActivityRecordAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: JuniorActivityRecordAdapter.Item, newItem: JuniorActivityRecordAdapter.Item): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.activityRecordItem?.description == newItem.activityRecordItem?.description &&
                oldItem.activityRecordItem?.status == newItem.activityRecordItem?.status &&
                oldItem.activityRecordItem?.title == newItem.activityRecordItem?.title
    }
}