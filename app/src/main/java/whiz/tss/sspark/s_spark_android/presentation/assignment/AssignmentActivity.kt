package whiz.tss.sspark.s_spark_android.presentation.assignment

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.AssignmentViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityAssignmentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class AssignmentActivity : BaseActivity() {

    private val viewModel: AssignmentViewModel by viewModel()

    private lateinit var binding: ActivityAssignmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        initView()

        viewModel.getAssignment()
    }

    override fun initView() {
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