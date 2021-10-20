package whiz.tss.sspark.s_spark_android.presentation.class_schedule.all_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.ClassScheduleAllClass
import whiz.sspark.library.data.entity.ClassScheduleAllClassDTO
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.StudentAllClassViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentClassScheduleAllClassBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseBottomSheetDialogFragment

class ClassScheduleAllClassBottomSheetDialog: BaseBottomSheetDialogFragment(){

    companion object {
        fun newInstance(term: Term) = ClassScheduleAllClassBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
            }
        }
    }

    private val viewModel: StudentAllClassViewModel by viewModel()

    private var _binding: FragmentClassScheduleAllClassBinding? = null
    private val binding get() = _binding!!

    private val term by lazy {
        arguments?.getString("term")!!.toObject<Term>()!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassScheduleAllClassBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewModel.getAllClasses(term.id)
    }

    override fun initView() {
        binding.vAllClass.init(
            term = resources.getString(R.string.class_schedule_term, term.term.toString(), convertToLocalizeYear(term.year)),
            onCloseClicked = {
                dismiss()
            },
            onRefresh = {
                viewModel.getAllClasses(term.id)
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAllClass.setSwipeRefreshLayout(isLoading)
        }
    }

    override fun observeData() {
        viewModel.allClassResponse.observe(this) {
            it?.let {
                updateAdapterItem(it)
            }
        }
    }

    override fun observeError() {
        viewModel.allClassErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem(allClasses: List<ClassScheduleAllClassDTO>) {
        val classes = allClasses.map {
            ClassScheduleAllClass(
                title = resources.getString(R.string.class_schedule_course_code_and_name, it.code, it.name),
                startDate = it.startAt,
                endDate = it.endAt
            )
        }

        binding.vAllClass.updateItem(classes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}