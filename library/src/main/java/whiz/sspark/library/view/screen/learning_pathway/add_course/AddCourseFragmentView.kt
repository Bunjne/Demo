package whiz.sspark.library.view.screen.learning_pathway.add_course

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ConcentrateCourseDTO
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.databinding.ViewAddCourseFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.getHighSchoolLevel
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.learning_pathway.add_course.AddCourseAdapter

class AddCourseFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAddCourseFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var concentrateCourses: List<ConcentrateCourseDTO> = listOf()
    private var selectedCourseIds: List<String> = listOf()
    private var currentSearchText = ""

    fun init(term: Term,
             selectedCourseIds: List<String>,
             currentCredit: Int,
             minCredit: Int,
             maxCredit: Int,
             onAddClicked: (String) -> Unit,
             onRefresh: () -> Unit) {
        this.selectedCourseIds = selectedCourseIds

        binding.ivClose.show(R.drawable.ic_close_bottom_sheet_dialog)
        binding.ivSearch.show(R.drawable.ic_search)

        binding.tvTerm.text = resources.getString(R.string.learning_pathway_header, getHighSchoolLevel(term.academicGrade).toString(), term.term.toString(), convertToLocalizeYear(term.year))
        binding.tvCredit.text = resources.getString(R.string.learning_pathway_add_course_preview_credit, currentCredit.toString(), minCredit.toString(), maxCredit.toString())

        with(binding.rvCourse) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = listOf(AddCourseAdapter.SELECTABLE_COURSE_VIEW_TYPE, AddCourseAdapter.UN_SELECTABLE_COURSE_VIEW_TYPE)
                    )
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = AddCourseAdapter(onAddClicked)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                currentSearchText = s.toString()
                updateAdapterIem()
            }
        })

        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }
    }

    fun setSwipeRefreshLoading(isLoading: Boolean?) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun updateConcentrateCourses(concentrateCourses: List<ConcentrateCourseDTO>) {
        this.concentrateCourses = concentrateCourses
        updateAdapterIem()
    }

    private fun updateAdapterIem() {
        val filteredConcentrateCourses = concentrateCourses.filter {
            it.courses.any {
                it.nameEn.contains(currentSearchText, true) ||
                it.nameTh.contains(currentSearchText, true) ||
                it.code.contains(currentSearchText, true)
            }
        }
        val item: MutableList<AddCourseAdapter.Item> = mutableListOf()

        filteredConcentrateCourses.forEach {
            item.add(AddCourseAdapter.Item(title = it.name))

            it.courses.filter {
                it.nameEn.contains(currentSearchText, true) ||
                it.nameTh.contains(currentSearchText, true) ||
                it.code.contains(currentSearchText, true)
            }.forEach {
                val course = Course(
                    id = it.id,
                    code = it.code,
                    credit = it.credit,
                    name = it.name
                )

                val isSelectable = !selectedCourseIds.contains(course.id)

                item.add(AddCourseAdapter.Item(course = course, isSelectable = isSelectable))
            }
        }

        (binding.rvCourse.adapter as? AddCourseAdapter)?.submitList(item)
    }
}