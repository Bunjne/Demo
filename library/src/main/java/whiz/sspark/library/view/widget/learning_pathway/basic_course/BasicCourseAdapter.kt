package whiz.sspark.library.view.widget.learning_pathway.basic_course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.databinding.ViewBasicCourseItemViewBinding

class BasicCourseAdapter(private val courses: List<Course>): RecyclerView.Adapter<BasicCourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicCourseViewHolder {
        return BasicCourseViewHolder(ViewBasicCourseItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BasicCourseViewHolder, position: Int) {
        val course = courses.getOrNull(position)
        val context = holder.itemView.context

        course?.let {
            holder.init(it)
            holder.itemView.background = when {
                itemCount == 1 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_single)
                position == 0 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_top)
                position == itemCount - 1 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_bottom)
                else -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_middle)
            }
        }
    }

    override fun getItemCount() = courses.size
}