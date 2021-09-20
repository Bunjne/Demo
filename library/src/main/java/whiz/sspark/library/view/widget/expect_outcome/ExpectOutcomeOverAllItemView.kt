package whiz.sspark.library.view.widget.expect_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.ExpectOutcomeOverall
import whiz.sspark.library.databinding.ViewExpectOutcomeOverAllItemViewBinding

class ExpectOutcomeOverAllItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewExpectOutcomeOverAllItemViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(overall: ExpectOutcomeOverall) {
        with(overall) {
            binding.vProgressBar.initGradientColor(startColor, endColor)
            binding.vProgressBar.init(value, indicators)
            binding.tvGPA.text = value.toString()

            binding.clContainer.setOnClickListener {
                binding.cvGPA.visibility = View.VISIBLE
                binding.cvGPA.postDelayed({
                    binding.cvGPA.visibility = View.GONE
                }, 4000)
            }
        }
    }
}