package whiz.tss.sspark.s_spark_android.presentation.school_record.activity_record

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.AdviseeActivityRecordViewModel

open class AdviseeActivityRecordFragment: ActivityRecordFragment() {

    companion object {
        fun newInstance(termId: String, studentId: String) = AdviseeActivityRecordFragment().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
                putString("studentId", studentId)
            }
        }
    }

    override val viewModel: AdviseeActivityRecordViewModel by viewModel()

    private val studentId by lazy {
        arguments?.getString("studentId")!!
    }

    override fun getActivityRecord() {
        viewModel.getActivityRecord(termId, studentId)
    }
}