package whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassMember

class LikeBySeenByItemAdapter(private val context: Context,
                              private val items: List<LikeBySeenByAdapterViewType>): RecyclerView.Adapter<LikeBySeenByItemAdapter.ViewHolder>() {

    sealed class LikeBySeenByAdapterViewType {
        class Header(val title: String, val membersNumber: Int): LikeBySeenByAdapterViewType()
        class Student(val member: ClassMember, val rank: Int): LikeBySeenByAdapterViewType()
        class Instructor(val member: ClassMember): LikeBySeenByAdapterViewType()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

        return when (viewType) {
            R.layout.view_like_by_seen_by_title_item -> ViewHolder(LikeBySeenByTitleItemView(context).apply {
                this.layoutParams = layoutParams
            })
            R.layout.view_like_by_seen_by_student_item -> ViewHolder(LikeBySeenByStudentItemVIew(context).apply {
                this.layoutParams = layoutParams
            })
            else -> ViewHolder(LikeBySeenByInstructorItemView(context).apply {
                this.layoutParams = layoutParams
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        item?.let {
            when {
                holder.itemView is LikeBySeenByTitleItemView && item is LikeBySeenByAdapterViewType.Header -> holder.itemView.init(item)
                holder.itemView is LikeBySeenByStudentItemVIew && item is LikeBySeenByAdapterViewType.Student -> holder.itemView.init(item)
                holder.itemView is LikeBySeenByInstructorItemView && item is LikeBySeenByAdapterViewType.Instructor -> holder.itemView.init(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is LikeBySeenByAdapterViewType.Header -> R.layout.view_like_by_seen_by_title_item
        is LikeBySeenByAdapterViewType.Student -> R.layout.view_like_by_seen_by_student_item
        is LikeBySeenByAdapterViewType.Instructor -> R.layout.view_like_by_seen_by_instructor_item
    }
}