package whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.LearningOutcome
import whiz.sspark.library.data.entity.LearningOutcomeDTO
import whiz.sspark.library.data.viewModel.LearningOutcomeViewModel
import whiz.sspark.library.extension.*
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.school_record.learning_outcome.JuniorLearningOutcomeAdapter
import whiz.tss.sspark.s_spark_android.databinding.FragmentJuniorLearningOutcomeBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome.JuniorExpectOutcomeBottomSheetDialog

open class JuniorLearningOutcomeFragment : BaseFragment() {

    companion object {
        internal const val EXPECT_OUTCOME_TAG = "ExpectOutcome"

        fun newInstance(termId: String) = JuniorLearningOutcomeFragment().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
            }
        }
    }

    protected open val viewModel: LearningOutcomeViewModel by viewModel()

    protected val termId by lazy {
        arguments?.getString("termId") ?: "0"
    }

    private var _binding: FragmentJuniorLearningOutcomeBinding? = null
    private val binding get() = _binding!!
    private var listener: OnRefresh? = null

    private var dataWrapper: DataWrapperX<Any>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentJuniorLearningOutcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = if (parentFragment != null) {
            parentFragment as OnRefresh
        } else {
            activity as OnRefresh
        }

        initView()

        if (savedInstanceState != null) {
            dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject()
        }

        if (dataWrapper != null) {
            val outcomes = dataWrapper?.data?.toJson()?.toObjects(Array<LearningOutcomeDTO>::class.java) ?: listOf()
            updateAdapterItem(outcomes)

            listener?.onSetLatestUpdatedText(dataWrapper)
        } else {
            getLearningOutcome()
        }
    }

    protected open fun getLearningOutcome() {
        viewModel.getLearningOutcome(termId)
    }

    override fun initView() {
        binding.vLearningOutcome.init(
            onRefresh = {
                getLearningOutcome()
            },
            onItemClicked = {
                val isShowing = childFragmentManager.findFragmentByTag(EXPECT_OUTCOME_TAG) != null
                if (!isShowing) {
                    showJuniorExpectOutcome(it)
                }
            }
        )
    }

    protected open fun showJuniorExpectOutcome(learningOutcome: LearningOutcome) {
        JuniorExpectOutcomeBottomSheetDialog.newInstance(
            termId = termId,
            courseId = learningOutcome.courseId,
            courseCode = learningOutcome.courseCode,
            courseName = learningOutcome.courseName,
            credit = learningOutcome.credit
        ).show(childFragmentManager, EXPECT_OUTCOME_TAG)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading->
            binding.vLearningOutcome.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            listener?.onSetLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.learningOutcomeResponse.observe(this) {
            it?.let {
                updateAdapterItem(it)
            }
        }
    }

    override fun observeError() {
        viewModel.learningOutcomeErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }
    }

    private fun updateAdapterItem(learningOutcomes: List<LearningOutcomeDTO>) {
        val items: MutableList<JuniorLearningOutcomeAdapter.Item> = mutableListOf()

        learningOutcomes.forEach { learningOutcome ->
            val titleItem = JuniorLearningOutcomeAdapter.Item(title = learningOutcome.name)
            items.add(titleItem)

            val startColor = learningOutcome.colorCode1.toColor(ContextCompat.getColor(requireContext(), R.color.primaryStartColor))
            val endColor = learningOutcome.colorCode2.toColor(ContextCompat.getColor(requireContext(), R.color.primaryEndColor))

            learningOutcome.courses.forEach {
                val learningOutcomeItem = JuniorLearningOutcomeAdapter.Item(
                    learningOutcome = LearningOutcome(
                        courseId = it.id,
                        startColor = startColor,
                        endColor = endColor,
                        credit = it.credits,
                        percentPerformance = it.percentPerformance,
                        courseCode = it.code,
                        courseName = it.name
                    )
                )

                items.add(learningOutcomeItem)
            }
        }

        binding.vLearningOutcome.updateItem(items)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            dataWrapper?.let {
                listener?.onSetLatestUpdatedText(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapper", dataWrapper?.toNullableJson())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnRefresh {
        fun onSetLatestUpdatedText(data: DataWrapperX<Any>?)
    }
}