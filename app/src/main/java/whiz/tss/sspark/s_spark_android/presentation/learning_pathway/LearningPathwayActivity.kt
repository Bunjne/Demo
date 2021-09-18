package whiz.tss.sspark.s_spark_android.presentation.learning_pathway

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.LearningPathwayDTO
import whiz.sspark.library.data.viewModel.LearningPathwayViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showApiResponseXAlert
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
                binding.vLearningPathway.updateItem(learningPathwaysDTO)
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
                viewModel.deleteCourse(termId = termId, courseId = courseId)
            },
            onShowRequiredCourseClicked = { term, courses ->
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
                binding.vLearningPathway.updateItem(it)
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