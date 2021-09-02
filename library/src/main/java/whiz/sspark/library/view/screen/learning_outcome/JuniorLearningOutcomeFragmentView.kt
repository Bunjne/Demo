package whiz.sspark.library.view.screen.learning_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.databinding.ViewJuniorLearningOutcomeFragmentBinding

class JuniorLearningOutcomeFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewJuniorLearningOutcomeFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init() {

    }
}