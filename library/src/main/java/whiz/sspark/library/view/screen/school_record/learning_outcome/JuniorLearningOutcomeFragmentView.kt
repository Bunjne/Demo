package whiz.sspark.library.view.screen.school_record.learning_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.databinding.ViewJuniorLearningOutcomeFragmentBinding
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.school_record.learning_outcome.JuniorLearningOutcomeAdapter

class JuniorLearningOutcomeFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewJuniorLearningOutcomeFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit,
             onItemClicked: (LearningOutcome) -> Unit) {
        binding.srlLearningOutcome.setOnRefreshListener {
            onRefresh()
        }

        with(binding.rvLearningOutcome) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = listOf(JuniorLearningOutcomeAdapter.PROGRESS_BAR_TYPE, JuniorLearningOutcomeAdapter.UNDER_EVALUATION_TYPE))
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = JuniorLearningOutcomeAdapter(context, onItemClicked)
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlLearningOutcome.isRefreshing = isLoading == true
    }

    fun updateItem(items: List<JuniorLearningOutcomeAdapter.Item>) {
        (binding.rvLearningOutcome.adapter as? JuniorLearningOutcomeAdapter)?.submitList(items)

        if (items.isNotEmpty()) {
            binding.tvNoRecord.visibility = View.GONE
            binding.rvLearningOutcome.visibility = View.VISIBLE
        } else {
            binding.tvNoRecord.visibility = View.VISIBLE
            binding.rvLearningOutcome.visibility = View.GONE
        }
    }
}