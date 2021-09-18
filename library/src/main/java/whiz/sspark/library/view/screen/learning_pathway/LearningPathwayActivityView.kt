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

        updateItem()
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateItem(learningPathwaysDTO: List<LearningPathwayDTO> = listOf()) {
        val items: MutableList<LearningPathwayAdapter.Item> = mutableListOf()

        learningPathwaysDTO.forEach { learningPathway ->
            val summaryCourseCredit = learningPathway.course.sumOf { it.credit }
            val summaryRequiredCourseCredit = learningPathway.requiredCourses.sumOf { it.credit }
            val credit = summaryCourseCredit + summaryRequiredCourseCredit

            val courseIds = learningPathway.course.map { it.id }
            val requiredCourseIds = learningPathway.requiredCourses.map { it.id }

            val selectedCourseIds = mutableListOf<String>()
            selectedCourseIds.addAll(courseIds)
            selectedCourseIds.addAll(requiredCourseIds)

            val header = LearningPathwayHeaderItem(
                term = learningPathway.term,
                currentCredit = credit,
                maxCredit = learningPathway.maxCredit,
                minCredit = learningPathway.minCredit,
                selectedCourseIds = selectedCourseIds
            )

            items.add(LearningPathwayAdapter.Item(header = header))

            learningPathway.course.forEach {
                val course = Course(
                    id = it.id,
                    code = it.code,
                    credit = it.credit,
                    name = it.name
                )

                val learningPathwayCourse = LearningPathwayCourseItem(
                    term = learningPathway.term,
                    course = course
                )

                items.add(LearningPathwayAdapter.Item(courseItem = learningPathwayCourse))
            }

            val course = learningPathway.requiredCourses.map {
                Course(
                    id = it.id,
                    code = it.code,
                    credit = it.credit,
                    name = it.name
                )
            }

            val requiredCourse = LearningPathwayRequiredCourse(
                term = learningPathway.term,
                courses = course
            )

            items.add(LearningPathwayAdapter.Item(requiredCourse = requiredCourse))
        }

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