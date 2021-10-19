package whiz.sspark.library.view.screen.collaboration.course_syllabus.week_detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewCourseSyllabusWeekDetailFragmentBinding
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
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
            if (itemDecorationCount == 0) {
                addItemDecoration(CustomDividerMultiItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_base_list_secondary)!!,
                    dividerViewType = listOf(
                        CourseSyllabusWeekAdapter.COURSE_DETAIL_TOP_VIEW_TYPE,
                        CourseSyllabusWeekAdapter.COURSE_DETAIL_MIDDLE_VIEW_TYPE,
                        CourseSyllabusWeekAdapter.COURSE_DETAIL_BOTTOM_VIEW_TYPE
                    )
                ))
            }

            layoutManager = LinearLayoutManager(context)
            adapter = CourseSyllabusWeekAdapter()
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun updateItem(item: List<CourseSyllabusWeekAdapter.Item>) {
        (binding.rvWeekDetail.adapter as? CourseSyllabusWeekAdapter)?.submitList(item)
    }
}