package whiz.sspark.library.view.widget.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewActivityRecordTitleWithDescriptionItemViewBinding

class JuniorActivityRecordTitleWithDescriptionItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewActivityRecordTitleWithDescriptionItemViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             description: String) {
        binding.tvTitle.text = title

        if (description.isNotBlank()) {
            binding.tvDescription.text = description
            binding.cvDescription.visibility = View.VISIBLE
        } else {
            binding.tvDescription.text = description
            binding.cvDescription.visibility = View.GONE
        }
    }
}