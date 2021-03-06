package whiz.sspark.library.view.widget.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewClassCreateAssignmentButtonBinding
import whiz.sspark.library.extension.show

class CreateAssignmentButtonView: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassCreateAssignmentButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onCreateAssignmentClicked: () -> Unit) {
        binding.ivActivity.show(R.drawable.ic_create_post)
        binding.root.setOnClickListener {
            onCreateAssignmentClicked()
        }
    }
}