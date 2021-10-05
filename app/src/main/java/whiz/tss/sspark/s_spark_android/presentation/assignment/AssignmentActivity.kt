package whiz.tss.sspark.s_spark_android.presentation.assignment

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.AssignmentViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityAssignmentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class AssignmentActivity : BaseActivity() {

    private val viewModel: AssignmentViewModel by viewModel { parametersOf() }

    private lateinit var binding: ActivityAssignmentBinding
    private lateinit var currentTerm: Term

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        lifecycleScope.launch {
            profileManager.term.collect {
                it?.let {
                    currentTerm = it
                }
            }
        }

        initView()

        viewModel.getAssignment(currentTerm.id)
    }

    override fun initView() {
        binding.vProfile.init(
            backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.bg_primary_gradient_0)!!,
            onBackPressed = {
                finish()
            }
        )
        binding.vAssignment.init()
    }

    override fun observeData() {
        viewModel.assignmentResponse.observe(this) {
            it?.let {
                lifecycleScope.launch {
                    binding.vAssignment.updateItem(it)
                }
            }
        }
    }

    override fun observeError() {
        viewModel.errorMessage.observe(this) {
            it?.let {
                showAlertWithOkButton(it)
            }
        }
    }
}