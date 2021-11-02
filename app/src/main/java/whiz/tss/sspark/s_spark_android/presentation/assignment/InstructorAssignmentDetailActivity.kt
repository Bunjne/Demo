package whiz.tss.sspark.s_spark_android.presentation.assignment

import android.app.Activity
import android.widget.PopupMenu
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.InstructorClassAssignmentDetailViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.utility.retrieveUserID

class InstructorAssignmentDetailActivity : AssignmentDetailActivity() {

    private val viewModel: InstructorClassAssignmentDetailViewModel by viewModel()

    private val userId by lazy {
        retrieveUserID(this)
    }

    private val options by lazy {
        resources.getStringArray(R.array.assignment_edit_assignment_options).toList()
    }

    private lateinit var popupMenu: PopupMenu

    override fun initView() {
        super.initView()

        val isCreator = if (userId.isNotBlank() && assignment.instructorId.isNotBlank()) {
            userId == assignment.instructorId
        } else {
            false
        }

        if (isCreator) {
            binding.vAssignment.showOptionView {
                if (::popupMenu.isInitialized)  {
                    popupMenu.show()
                } else {
                    popupMenu = PopupMenu(this, it).apply {
                        setOnMenuItemClickListener {
                            when (it.title) {
                                resources.getString(R.string.general_delete_text) -> viewModel.deleteAssignment(assignment.classGroupId, assignment.id)
                                resources.getString(R.string.general_edit_text) -> {
                                //TODO wait create assignment screen
                                }
                            }

                            true
                        }

                        menu.clear()

                        options.forEach { option ->
                            menu.add(option)
                        }
                    }

                    popupMenu.show()
                }
            }
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    override fun observeData() {
        viewModel.deleteAssignmentResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun observeError() {
        viewModel.deleteAssignmentErrorResponse.observe(this) {
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
}