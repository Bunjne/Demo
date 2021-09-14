package whiz.sspark.library.view.screen.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.databinding.ViewAbilityFragmentBinding
import whiz.sspark.library.view.widget.school_record_activity.SeniorActivityRecordAdapter

class AbilityFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAbilityFragmentBinding.inflate(LayoutInflater.from(context), this, false)
    }

    fun init(onRefresh: () -> Unit) {
        with(binding.rvActivityRecord) {
            layoutManager = LinearLayoutManager(context)
            adapter = SeniorActivityRecordAdapter()
        }

        binding.srlLearningOutcome.setOnRefreshListener {
            updateItem()
            onRefresh()
        }
    }

    fun updateItem() {
//        if (learningOutcomes.isNotEmpty()) { TODO uncomment when API spec available
//            binding.tvNoRecord.visibility = View.GONE
//            binding.rvActivityRecord.visibility = View.VISIBLE
//        } else {
//            binding.tvNoRecord.visibility = View.VISIBLE
//            binding.rvActivityRecord.visibility = View.GONE
//        }
    }
}