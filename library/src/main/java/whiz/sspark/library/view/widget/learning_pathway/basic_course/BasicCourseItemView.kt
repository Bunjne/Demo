package whiz.sspark.library.view.widget.learning_pathway.basic_course

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.databinding.ViewBasicCourseItemViewBinding

class BasicCourseViewHolder(
    private val binding: ViewBasicCourseItemViewBinding
): RecyclerView.ViewHolder(binding.root) {
    fun init(course: Course) {
        with(course) {
            binding.tvCourseCode.text = code
            binding.tvCourseName.text = name
            binding.tvCredit.text = binding.root.context.resources.getString(R.string.general_credit, credit.toString())
        }
    }
}