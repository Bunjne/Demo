package whiz.sspark.library.view.screen.learning_pathway

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.databinding.ViewLearningPathwayActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.learning_pathway.LearningPathwayAdapter

class LearningPathwayActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningPathwayActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onAddCourseClicked: (Term, Int, Int, Int, List<String>) -> Unit,
             onDeleteCourseClicked: (String, String) -> Unit,
             onShowRequiredCourseClicked: (Term, List<Course>) -> Unit,
             onRefresh: () -> Unit) {
        with(binding.rvCourse) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_base_list_secondary)!!,
                        dividerViewType = LearningPathwayAdapter.courseViewTypes
                    )
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = LearningPathwayAdapter(
                onAddCourseClicked = onAddCourseClicked,
                onDeleteCourseClicked = onDeleteCourseClicked,
                onShowRequiredCourseClicked = onShowRequiredCourseClicked
            )
        }

        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateItem(items: List<LearningPathwayAdapter.Item> = listOf()) {
        (binding.rvCourse.adapter as? LearningPathwayAdapter)?.submitList(items)

        if (items.isNotEmpty()) {
            binding.rvCourse.visibility = View.VISIBLE
            binding.tvNoCourse.visibility = View.GONE
        } else {
            binding.rvCourse.visibility = View.GONE
            binding.tvNoCourse.visibility = View.VISIBLE
        }
    }
}