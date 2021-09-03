package whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.viewModel.LearningOutcomeViewModel
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.FragmentJuniorLearningOutcomeBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class JuniorLearningOutcomeFragment : BaseFragment() {

    companion object {
        fun newInstance(currentSemesterId: Int) = JuniorLearningOutcomeFragment().apply {
            arguments = Bundle().apply {
                putInt("currentSemesterId", currentSemesterId)
            }
        }
    }

    private val viewModel: LearningOutcomeViewModel by viewModel()

    private val currentSemesterId by lazy {
        arguments?.getInt("currentSemesterId") ?: 0
    }

    private var _binding: FragmentJuniorLearningOutcomeBinding? = null
    private val binding get() = _binding!!
    private var listener: OnRefresh? = null

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

        viewModel.getLearningOutcome(currentSemesterId)
    }

    override fun initView() {
        binding.vLearningOutcome.init(
            onRefresh = {
                viewModel.getLearningOutcome(currentSemesterId)
            },
            onItemClicked = {
                //TODO wait implement EO
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) {
            binding.vLearningOutcome.setSwipeRefreshLoading(it)
        }

        viewModel.viewRendering.observe(this) {
            listener?.onRefresh(it)
        }
    }

    override fun observeData() {
        viewModel.learningOutcomeResponse.observe(this) {
            it?.let {
                binding.vLearningOutcome.updateItem(it)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnRefresh {
        fun onRefresh(data: DataWrapperX<Any>?)
    }
}