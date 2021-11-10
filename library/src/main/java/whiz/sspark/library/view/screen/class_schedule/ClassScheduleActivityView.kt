package whiz.sspark.library.view.screen.class_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.WeekSelection
import whiz.sspark.library.databinding.ViewClassScheduleActivityBinding
import whiz.sspark.library.extension.show
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
             title: String,
             onTermClicked: () -> Unit,
             onAllClassesClicked: () -> Unit,
             onPreviousWeekClicked: () -> Unit,
             onNextWeekClicked: () -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTerm.text = term
        binding.tvTitle.text = title
        binding.ivDropdown.show(R.drawable.ic_dropdown)

        with(binding.ivAllClasses) {
            show(R.drawable.ic_tab)
            setOnClickListener {
                onAllClassesClicked()
            }
        }

        binding.cvTerm.setOnClickListener {
            if (binding.ivDropdown.visibility == View.VISIBLE) {
                onTermClicked()
            }
        }

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

    fun updateTerm(term: String) {
        binding.tvTerm.text = term
    }

    fun initMultipleTerm(isTermSelectable: Boolean, onInitPopupMenu: (View) -> Unit) {
        if (isTermSelectable) {
            binding.ivDropdown.visibility = View.VISIBLE
            onInitPopupMenu(binding.cvTerm)
        } else {
            binding.ivDropdown.visibility = View.GONE
        }
    }

    fun showAdviseeProfile(advisee: Advisee) {
        binding.vAdviseeProfile.isVisible = true
        binding.vAdviseeProfile.init(advisee = advisee)
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