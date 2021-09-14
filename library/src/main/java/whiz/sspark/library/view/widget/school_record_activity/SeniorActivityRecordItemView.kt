package whiz.sspark.library.view.widget.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.ActivityRecordProgressItem
import whiz.sspark.library.databinding.ViewAbilityItemBinding

class SeniorActivityRecordItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAbilityItemBinding.inflate(LayoutInflater.from(context), this, false)
    }

    fun init(activityRecordProgressItem: ActivityRecordProgressItem) {
        with(activityRecordProgressItem) {
            binding.tvTitle.text = title
            binding.vProgressBar.init(
                currentProgress = value,
                titles = indicators
            )
        }
    }
}