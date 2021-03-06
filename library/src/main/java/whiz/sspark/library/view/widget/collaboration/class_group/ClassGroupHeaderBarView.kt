package whiz.sspark.library.view.widget.collaboration.class_group

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewClassGroupHeaderBarBinding
import whiz.sspark.library.extension.show

class ClassGroupHeaderBarView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassGroupHeaderBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             iconUrl: String,
             gradientColor: IntArray) {
        binding.tvClassGroupTitle.text = title

        if (iconUrl.isBlank()) {
            binding.ivClassGroupIcon.visibility = View.GONE
        } else {
            binding.ivClassGroupIcon.visibility = View.VISIBLE
            binding.ivClassGroupIcon.show(iconUrl)
        }

        binding.cvHeaderBar.background_Gradient_Colors = gradientColor
        binding.cvHeaderBar.invalidate()
    }
}