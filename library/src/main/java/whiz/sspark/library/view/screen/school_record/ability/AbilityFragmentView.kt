package whiz.sspark.library.view.screen.school_record.ability

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewAbilityFragmentBinding
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.school_record.ability.AbilityAdapter

class AbilityFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAbilityFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit) {
        with(binding.rvActivityRecord) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = AbilityAdapter.PROGRESS_BAR_VIEW_TYPE
                    )
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = AbilityAdapter()
        }

        binding.srlLearningOutcome.setOnRefreshListener {
            onRefresh()
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlLearningOutcome.isRefreshing = isLoading == true
    }

    fun updateItem(items: List<AbilityAdapter.Item>) {
        (binding.rvActivityRecord.adapter as? AbilityAdapter)?.submitList(items)

        if (items.isEmpty()) {
            binding.tvNoRecord.visibility = View.VISIBLE
            binding.rvActivityRecord.visibility = View.GONE
        } else {
            binding.tvNoRecord.visibility = View.GONE
            binding.rvActivityRecord.visibility = View.VISIBLE
        }
    }
}