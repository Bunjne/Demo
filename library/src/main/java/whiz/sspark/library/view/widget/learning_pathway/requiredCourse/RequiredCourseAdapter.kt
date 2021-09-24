package whiz.sspark.library.view.widget.learning_pathway.requiredCourse

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Course

class RequiredCourseAdapter(private val courses: List<Course>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(RequiredCourseItemView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val course = courses.getOrNull(position)

        course?.let {
            (holder.itemView as? RequiredCourseItemView)?.apply {
                init(it)

                background = when {
                    itemCount == 1 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_single)
                    position == 0 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_top)
                    position == itemCount - 1 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_bottom)
                    else -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_middle)
                }
            }
        }
    }

    override fun getItemCount() = courses.size
}