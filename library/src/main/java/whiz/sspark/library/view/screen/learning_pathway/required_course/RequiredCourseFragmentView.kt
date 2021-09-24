package whiz.sspark.library.view.screen.learning_pathway.required_course

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.databinding.ViewRequiredCourseFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.getHighSchoolLevel
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.learning_pathway.requiredCourse.RequiredCourseAdapter

class RequiredCourseFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewRequiredCourseFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(term: Term,
             courses: List<Course>,
             onCloseClicked: () -> Unit) {
        with(binding.ivClose) {
            show(R.drawable.ic_close_bottom_sheet_dialog)
            setOnClickListener {
                onCloseClicked()
            }
        }

        binding.tvTerm.text = resources.getString(R.string.learning_pathway_header, getHighSchoolLevel(term.academicGrade).toString(), term.term.toString(), convertToLocalizeYear(term.year))

        with(binding.rvCourse) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = 0
                    )
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = RequiredCourseAdapter(courses)
        }
    }
}