package whiz.tss.sspark.s_spark_android.presentation.school_record.ability

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.AdviseeAbilityViewModel
import whiz.tss.sspark.s_spark_android.R

class AdviseeAbilityFragment: AbilityFragment() {

    companion object {
        fun newInstance(termId: String, studentId: String) = AdviseeAbilityFragment().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
                putString("studentId", studentId)
            }
        }
    }

    override val viewModel: AdviseeAbilityViewModel by viewModel()

    private val studentId by lazy {
        arguments?.getString("studentId")!!
    }

    override fun getAbility() {
        viewModel.getAbility(termId, studentId)
    }
}