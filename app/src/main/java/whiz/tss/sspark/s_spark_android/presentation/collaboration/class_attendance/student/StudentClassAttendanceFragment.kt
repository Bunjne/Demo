package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_attendance.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.StudentClassAttendanceViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentClassAttendanceBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class StudentClassAttendanceFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String) = StudentClassAttendanceFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
            }
        }
    }

    private val viewModel: StudentClassAttendanceViewModel by viewModel()

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private var _binding: FragmentStudentClassAttendanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStudentClassAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            initView()

            viewModel.getClassAttendance(classGroupId)
        }
    }

    override fun initView() {
        binding.vAttendance.init {
            viewModel.getClassAttendance(classGroupId)
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vAttendance.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.classAttendanceResponse.observe(this, Observer {
            it?.let {
                binding.vAttendance.renderAttendance(it)
            }
        })
    }

    override fun observeError() {
        viewModel.classAttendanceErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }

            binding.vAttendance.setNoAttendanceVisibility(true)
        })

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }
}