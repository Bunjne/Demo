package whiz.tss.sspark.s_spark_android.presentation.class_schedule.all_class

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.AdviseeClassScheduleAllClassViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject

class AdviseeClassScheduleAllClassBottomSheetDialog: StudentClassScheduleAllClassBottomSheetDialog() {

    companion object {
        fun newInstance(term: Term, studentId: String, advisee: Advisee) = AdviseeClassScheduleAllClassBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
                putString("studentId", studentId)
                putString("advisee", advisee.toJson())
            }
        }
    }

    override val viewModel: AdviseeClassScheduleAllClassViewModel by viewModel()

    private val studentId: String by lazy {
        arguments?.getString("studentId") ?: ""
    }

    private val advisee: Advisee by lazy {
        arguments?.getString("advisee")?.toObject<Advisee>()!!
    }

    override fun initView() {
        super.initView()
        binding.vAllClass.setAdvisee(advisee)
    }

    override fun getAllClasses() {
        viewModel.getAllClasses(term.id, studentId)
    }
}