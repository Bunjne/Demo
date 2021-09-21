package whiz.tss.sspark.s_spark_android.presentation.learning_pathway

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.LearningPathwayViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlert
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.learning_pathway.LearningPathwayAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityLearningPathwayBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.learning_pathway.add_course.AddCourseBottomSheetDialog
import whiz.tss.sspark.s_spark_android.presentation.learning_pathway.required_course.RequiredCourseBottomSheetDialog

class LearningPathwayActivity : BaseActivity(), AddCourseBottomSheetDialog.OnClickListener {

    companion object {
        private val ADD_COURSE_DIALOG = "AddCourseDialog"
        private val REQUIRED_COURSE_DIALOG = "RequiredCourseDialog"
    }

    private val viewModel: LearningPathwayViewModel by viewModel()

    private lateinit var binding: ActivityLearningPathwayBinding

    private var dataWrapper: DataWrapperX<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningPathwayBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        initView()

        if (savedInstanceState != null) {
            onSaveInstanceState(savedInstanceState)

            if (dataWrapper != null) {
                val learningPathwaysDTO = dataWrapper?.data?.toJson()?.toObjects(Array<LearningPathwayDTO>::class.java) ?: listOf()

                binding.vLearningPathway.setLatestUpdatedText(dataWrapper)
                updateAdapterItem(learningPathwaysDTO)
            } else {
                viewModel.getLearningPathway()
            }
        } else {
            viewModel.getLearningPathway()
        }
    }

    override fun initView() {
        binding.vLearningPathway.init(
            onAddCourseClicked = { term, currentCredit, minCredit, maxCredit, selectedCourseIds ->
                if (viewModel.viewLoading.value == true) {
                    return@init
                }

                val isShowing = supportFragmentManager.findFragmentByTag(ADD_COURSE_DIALOG) != null

                if (!isShowing) {
                    AddCourseBottomSheetDialog.newInstance(
                        term = term,
                        currentCredit = currentCredit,
                        minCredit = minCredit,
                        maxCredit = maxCredit,
                        selectedCourseIds = selectedCourseIds
                    ).show(supportFragmentManager, ADD_COURSE_DIALOG)
                }
            },
            onDeleteCourseClicked = { termId, courseId ->
                if (viewModel.viewLoading.value == true) {
                    return@init
                }

                showAlert(
                    title = resources.getString(R.string.general_confirm_to_delete_text),
                    positiveTitle = resources.getString(R.string.general_delete_text),
                    onPositiveClicked = {
                        viewModel.deleteCourse(termId = termId, courseId = courseId)
                    },
                    negativeTitle = resources.getString(R.string.general_text_cancel)
                )
            },
            onShowRequiredCourseClicked = { term, courses ->
                if (viewModel.viewLoading.value == true) {
                    return@init
                }

                val isShowing = supportFragmentManager.findFragmentByTag(REQUIRED_COURSE_DIALOG) != null

                if (!isShowing) {
                    RequiredCourseBottomSheetDialog.newInstance(
                        term = term,
                        courses = courses
                    ).show(supportFragmentManager, REQUIRED_COURSE_DIALOG)
                }
            },
            onRefresh = {
                viewModel.getLearningPathway()
            }
        )
    }

    override fun observeView() {
        viewModel.courseLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vLearningPathway.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vLearningPathway.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.learningPathwayResponse.observe(this) {
            it?.let {
                updateAdapterItem(it)
            }
        }

        viewModel.addCourseResponse.observe(this) {
            it?.let {
                viewModel.getLearningPathway()
            }
        }

        viewModel.deleteCourseResponse.observe(this) {
            it?.let {
                viewModel.getLearningPathway()
            }
        }
    }

    override fun observeError() {
        listOf(
            viewModel.learningPathwayErrorResponse,
            viewModel.addCourseErrorResponse,
            viewModel.deleteCourseErrorResponse
        ).forEach {
            it.observe(this) {
                it?.let {
                    showApiResponseXAlert(this, it)
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem(learningPathways: List<LearningPathwayDTO>) {
        val items: MutableList<LearningPathwayAdapter.Item> = mutableListOf()

        learningPathways.forEach { learningPathway ->
            val summaryCourseCredit = learningPathway.course.sumOf { it.credit }
            val summaryRequiredCourseCredit = learningPathway.requiredCourses.sumOf { it.credit }
            val credit = summaryCourseCredit + summaryRequiredCourseCredit

            val courseIds = learningPathway.course.map { it.id }
            val requiredCourseIds = learningPathway.requiredCourses.map { it.id }

            val selectedCourseIds = mutableListOf<String>()
            selectedCourseIds.addAll(courseIds)
            selectedCourseIds.addAll(requiredCourseIds)

            val header = LearningPathwayHeaderItem(
                term = learningPathway.term,
                currentCredit = credit,
                maxCredit = learningPathway.maxCredit,
                minCredit = learningPathway.minCredit,
                selectedCourseIds = selectedCourseIds
            )

            items.add(LearningPathwayAdapter.Item(header = header))

            learningPathway.course.forEach {
                val course = Course(
                    id = it.id,
                    code = it.code,
                    credit = it.credit,
                    name = it.name
                )

                val learningPathwayCourse = LearningPathwayCourseItem(
                    term = learningPathway.term,
                    course = course
                )

                items.add(LearningPathwayAdapter.Item(courseItem = learningPathwayCourse))
            }

            val courses = learningPathway.requiredCourses.map {
                Course(
                    id = it.id,
                    code = it.code,
                    credit = it.credit,
                    name = it.name
                )
            }

            val requiredCourse = LearningPathwayRequiredCourseItem(
                term = learningPathway.term,
                courses = courses
            )

            items.add(LearningPathwayAdapter.Item(requiredCourseItem = requiredCourse))
        }

        binding.vLearningPathway.updateItem(items)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapper", dataWrapper.toString())
    }

    override fun onAddCourseClicked(termId: String, courseId: String) {
        viewModel.addCourse(
            termId = termId,
            courseId = courseId
        )
    }
}