package whiz.sspark.library.view.widget.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewJuniorActivityRecordTitleWithDescriptionItemViewBinding

class JuniorActivityRecordTitleWithDescriptionItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewJuniorActivityRecordTitleWithDescriptionItemViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             description: String) {
        binding.tvTitle.text = title
        binding.tvDescription.text = description
    }
}