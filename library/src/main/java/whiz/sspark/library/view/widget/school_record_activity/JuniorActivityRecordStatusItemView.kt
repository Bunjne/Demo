package whiz.sspark.library.view.widget.school_record_activity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewActivityRecordStatusItemViewBinding
import whiz.sspark.library.extension.show

class JuniorActivityRecordStatusItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewActivityRecordStatusItemViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(status: Boolean,
             title: String) {
        if (status) {
            binding.ivStatus.show(R.drawable.ic_done)
        } else {
            binding.ivStatus.show(R.drawable.ic_cancel_grey)
        }

        binding.tvTitle.text = title
    }
}