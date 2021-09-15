package whiz.sspark.library.view.screen.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ActivityDTO
import whiz.sspark.library.data.entity.ActivityRecordItem
import whiz.sspark.library.databinding.ViewActivityRecordFragmentBinding
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.learning_outcome.JuniorLearningOutcomeAdapter
import whiz.sspark.library.view.widget.school_record_activity.ActivityRecordAdapter

class ActivityRecordFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewActivityRecordFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit) {
        with(binding.rvActivityRecord) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = listOf(
                            ActivityRecordAdapter.TITLE_WITH_DESCRIPTION_VIEW_TYPE,
                            ActivityRecordAdapter.STATUS_WITH_DESCRIPTON_VIEW_TYPE,
                            ActivityRecordAdapter.STATUS_VIEW_TYPE
                        )
                    )
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = ActivityRecordAdapter()
        }

        binding.srlLearningOutcome.setOnRefreshListener {
            updateItem()
            onRefresh()
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlLearningOutcome.isRefreshing = isLoading == true
    }

    fun updateItem(activities: List<ActivityDTO> = listOf()) {
        val items: MutableList<ActivityRecordAdapter.Item> = mutableListOf()

        activities.forEach {
            items.add(ActivityRecordAdapter.Item(title = it.name))

            it.activities.forEach {
                val activity = ActivityRecordItem(
                    title = it.name,
                    status = it.isCompleted,
                    description = it.comment
                )

                items.add(ActivityRecordAdapter.Item(activity = activity))
            }
        }

        (binding.rvActivityRecord.adapter as? ActivityRecordAdapter)?.submitList(items)

        if (activities.isEmpty()) {
            binding.tvNoRecord.visibility = View.VISIBLE
            binding.rvActivityRecord.visibility = View.GONE
        } else {
            binding.tvNoRecord.visibility = View.GONE
            binding.rvActivityRecord.visibility = View.VISIBLE
        }
    }
}