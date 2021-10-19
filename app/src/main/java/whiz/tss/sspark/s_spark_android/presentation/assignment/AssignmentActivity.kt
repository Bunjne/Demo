package whiz.tss.sspark.s_spark_android.presentation.assignment

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.static.PagingConfiguration
import whiz.sspark.library.data.viewModel.AssignmentViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityAssignmentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class AssignmentActivity : BaseActivity() {

    private val viewModel: AssignmentViewModel by viewModel { parametersOf() }

    private lateinit var binding: ActivityAssignmentBinding
    private lateinit var currentTerm: Term

    private var dataWrapper: DataWrapperX<Any>? = null
    private var currentPage = PagingConfiguration.INITIAL_PAGE
    private var totalPage = PagingConfiguration.INITIAL_TOTAL_PAGE
    private var assignments = mutableListOf<AssignmentItemDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
            initView()
            binding.vAssignment.setLatestUpdatedText(dataWrapper)
            updateAdapterItem()
        } else {
            lifecycleScope.launch {
                profileManager.term.collect {
                    it?.let {
                        currentTerm = it

                        initView()

                        viewModel.getLatestAssignments(currentTerm.id, PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)

                    }
                }
            }
        }
    }

    override fun initView() {
        binding.vProfile.init(
            backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.bg_primary_gradient_0)!!,
            onBackPressed = {
                finish()
            }
        )

        binding.vAssignment.init(
            onAssignmentClicked = { assignment ->
                val intent = Intent(this, AssignmentDetailActivity::class.java).apply {
                    putExtra("assignment", assignment.toJson())
                }

                startActivity(intent)
            },
            onRefresh = {
                viewModel.getLatestAssignments(currentTerm.id, PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
            },
            onLoadMore = {
                val nextPage = currentPage + PagingConfiguration.PAGE_INCREASE_STEP
                viewModel.getAssignments(currentTerm.id, nextPage, PagingConfiguration.PAGE_SIZE)
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAssignment.setSwipeRefreshLayout(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vAssignment.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.latestAssignmentResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                currentPage = PagingConfiguration.INITIAL_PAGE
                totalPage = it.totalPage

                with(assignments) {
                    clear()
                    addAll(it.items)
                }

                checkIsLastPage()
                updateAdapterItem()
            }
        }

        viewModel.assignmentResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                currentPage += PagingConfiguration.PAGE_INCREASE_STEP

                assignments.addAll(it.items)

                checkIsLastPage()
                updateAdapterItem()
            }
        }
    }

    override fun observeError() {
        viewModel.assignmentErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem() {
        val convertedAssignments = assignments.map { it.convertToAssignment() }
        binding.vAssignment.updateItem(convertedAssignments)
    }

    private fun checkIsLastPage() {
        val isLastPage = currentPage >= totalPage
        binding.vAssignment.setIsLastPage(isLastPage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject()
        currentTerm = savedInstanceState.getString("currentTerm")?.toObject()!!
        assignments = savedInstanceState.getString("assignments")?.toObjects(Array<AssignmentItemDTO>::class.java) ?: mutableListOf()
        currentPage = savedInstanceState.getInt("currentPage")
        totalPage = savedInstanceState.getInt("totalPage")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapper", dataWrapper?.toJson())
        outState.putString("assignments", assignments.toJson())
        outState.putString("currentTerm", currentTerm.toJson())
        outState.putInt("currentPage", currentPage)
        outState.putInt("totalPage", totalPage)
    }
}