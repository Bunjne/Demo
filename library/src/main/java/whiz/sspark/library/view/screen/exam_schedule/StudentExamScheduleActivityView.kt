package whiz.sspark.library.view.screen.exam_schedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewStudentExamScheduleActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.exam_schedule.ExamScheduleAdapter

class StudentExamScheduleActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewStudentExamScheduleActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(term: String,
             segmentTitles: List<String>,
             onSegmentClicked: (Int) -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTerm.text = term
        binding.vSegment.init(segmentTitles, onSegmentClicked)
        binding.srlContainer.setOnRefreshListener(onRefresh)

        with(binding.rvSchedule) {
            addItemDecoration(
                CustomDividerItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = ExamScheduleAdapter.COURSE_VIEW_TYPE
                )
            )

            layoutManager = LinearLayoutManager(context)
            adapter = ExamScheduleAdapter()
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateItem(items: List<ExamScheduleAdapter.Item>) {
        (binding.rvSchedule.adapter as? ExamScheduleAdapter)?.submitList(items) {
            binding.rvSchedule.scrollToPosition(0)
        }
    }
}