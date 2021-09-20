package whiz.tss.sspark.s_spark_android.presentation.collaboration.course_syllabus.week_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.CourseSyllabusWeekDetailViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.FragmentCourseSyllabusWeekDetailBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class CourseSyllabusWeekDetailFragment: BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String) = CourseSyllabusWeekDetailFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    private val viewModel: CourseSyllabusWeekDetailViewModel by viewModel()

    private var _binding: FragmentCourseSyllabusWeekDetailBinding? = null
    private val binding get() = _binding!!

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private var listener: OnCloseListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseSyllabusWeekDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = if (parentFragment != null) {
            parentFragment as OnCloseListener
        } else {
            activity as OnCloseListener
        }

        initView()

        viewModel.getCourseWeekDetail(classGroupId)
    }

    override fun initView() {
        binding.vWeekDetail.init {
            viewModel.getCourseWeekDetail(classGroupId)
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vWeekDetail.setSwipeRefreshLayout(isLoading)
        }
    }

    override fun observeData() {
        viewModel.courseWeekDetailResponse.observe(this) {
            it?.let {
//                binding.vDetail.updateItem() TODO wait confirm object from API
            }
        }
    }

    override fun observeError() {
        viewModel.courseWeekDetailErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireActivity(), it) {
                    listener?.onClose()
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireActivity().showAlertWithOkButton(it) {
                    listener?.onClose()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnCloseListener {
        fun onClose()
    }
}