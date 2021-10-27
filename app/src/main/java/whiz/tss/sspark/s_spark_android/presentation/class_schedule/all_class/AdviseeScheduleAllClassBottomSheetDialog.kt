package whiz.tss.sspark.s_spark_android.presentation.class_schedule.all_class

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.AdviseeAllStudentClassViewModel
import whiz.sspark.library.extension.toJson

class AdviseeScheduleAllClassBottomSheetDialog: ClassScheduleAllClassBottomSheetDialog() {

    companion object {
        fun newInstance(term: Term, studentId: String) = ClassScheduleAllClassBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
                putString("studentId", studentId)
            }
        }
    }

    private val viewModel: AdviseeAllStudentClassViewModel by viewModel()

    private val studentId: String by lazy {
        arguments?.getString("studentId") ?: ""
    }

    override fun getAllClasses() {
        viewModel.getAllClasses(term.id, studentId)
    }
}