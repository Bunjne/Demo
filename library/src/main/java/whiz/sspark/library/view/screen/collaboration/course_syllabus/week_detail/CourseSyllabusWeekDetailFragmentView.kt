package whiz.sspark.library.view.screen.collaboration.course_syllabus.week_detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.databinding.ViewCourseSyllabusWeekDetailFragmentBinding
import whiz.sspark.library.view.widget.collaboration.course_syllabus.detail.CourseSyllabusAdapter
import whiz.sspark.library.view.widget.collaboration.course_syllabus.week.CourseSyllabusWeekAdapter

class CourseSyllabusWeekDetailFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCourseSyllabusWeekDetailFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit) {
        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }

        with(binding.rvWeekDetail) {
            layoutManager = LinearLayoutManager(context)
            adapter = CourseSyllabusWeekAdapter()
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun updateItem(item: List<CourseSyllabusAdapter.Item>) {
        (binding.rvWeekDetail.adapter as? CourseSyllabusAdapter)?.submitList(item)
    }
}