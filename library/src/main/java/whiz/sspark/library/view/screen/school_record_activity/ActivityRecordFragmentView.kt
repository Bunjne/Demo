package whiz.sspark.library.view.screen.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewActivityRecordFragmentBinding

class ActivityRecordFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewActivityRecordFragmentBinding.inflate(LayoutInflater.from(context), this, false)
    }

    fun init(onRefresh: () -> Unit) {

        binding.srlLearningOutcome.setOnRefreshListener {
            onRefresh()
        }
    }

    fun updateItem() {

    }
}