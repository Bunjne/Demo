package whiz.sspark.library.view.widget.learning_pathway

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.LearningPathwayCourseItem
import whiz.sspark.library.databinding.ViewLearningPathwayCourseSingleItemBinding
import whiz.sspark.library.extension.show

class LearningPathwayCourseSingleItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningPathwayCourseSingleItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(courseItem: LearningPathwayCourseItem,
             onDeleteClicked: (String, String) -> Unit) {
        with(binding.ivDelete) {
            show(R.drawable.ic_bin)
            setOnClickListener {
                onDeleteClicked(courseItem.term.id, courseItem.course.id)
            }
        }

        with(courseItem.course) {
            binding.tvCourseCode.text = code
            binding.tvCourseName.text = name
            binding.tvCredit.text = resources.getString(R.string.general_credit, credit.toString())
        }
    }
}