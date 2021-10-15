package whiz.sspark.library.view.widget.learning_pathway

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.LearningPathwayHeaderItem
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.databinding.ViewLearningPathwayHeaderItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.getHighSchoolLevel

class LearningPathwayHeaderItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewLearningPathwayHeaderItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(header: LearningPathwayHeaderItem,
             onAddClicked: (Term, Int, Int, Int, List<String>) -> Unit,
             onShowRequiredCourseClicked: (Term, List<Course>) -> Unit) {
        binding.ivLock.show(R.drawable.ic_lock)
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        with(binding.ivAdd) {
            show(R.drawable.ic_add)
            setOnClickListener {
                onAddClicked(header.term, header.currentCredit, header.minCredit, header.maxCredit, header.selectedCourseIds)
            }
        }

        with(header) {
            binding.tvTerm.text = resources.getString(R.string.learning_pathway_header, getHighSchoolLevel(term.academicGrade).toString(), term.term.toString(), convertToLocalizeYear(term.year))
            binding.tvCredit.text = resources.getString(R.string.general_credit, currentCredit.toString())
            binding.tvMinMaxCredit.text = resources.getString(R.string.learning_pathway_term_credit, minCredit.toString(), maxCredit.toString())
            binding.tvCourseCount.text = resources.getString(R.string.learning_pathway_required_course_count, requiredCourses.size.toString())

            binding.cvRequiredCourse.setOnClickListener {
                onShowRequiredCourseClicked(term, requiredCourses)
            }
        }
    }
}