package whiz.sspark.library.view.screen.class_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.WeekSelection
import whiz.sspark.library.databinding.ViewClassScheduleActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.class_schedule.ClassScheduleAdapter
import whiz.sspark.library.view.widget.class_schedule.WeekSelectionAdapter

class ClassScheduleActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassScheduleActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var weekSelectionAdapter: WeekSelectionAdapter? = null
    private var classScheduleAdapter: ClassScheduleAdapter? = null

    fun init(term: String,
             onPreviousWeekClicked: () -> Unit,
             onNextWeekClicked: () -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTerm.text = term

        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()

        weekSelectionAdapter = WeekSelectionAdapter(
            onPreviousWeekClicked = onPreviousWeekClicked,
            onNextWeekClicked = onNextWeekClicked
        )

        classScheduleAdapter = ClassScheduleAdapter()

        val concatAdapter = ConcatAdapter(config, weekSelectionAdapter, classScheduleAdapter)

        with(binding.rvSchedule) {
            addItemDecoration(
                CustomDividerItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = ClassScheduleAdapter.COURSE_VIEW_TYPE
                )
            )

            this.layoutManager = LinearLayoutManager(context)
            adapter = concatAdapter
        }

        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateSelectedWeek(weeks: WeekSelection) {
        weekSelectionAdapter?.submitList(listOf(weeks))
    }

    fun updateSchedule(item: List<ClassScheduleAdapter.Item>) {
        classScheduleAdapter?.submitList(item)
    }
}