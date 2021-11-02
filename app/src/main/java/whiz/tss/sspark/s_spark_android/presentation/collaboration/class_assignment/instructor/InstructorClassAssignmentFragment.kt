package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_assignment.instructor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.static.PagingConfiguration
import whiz.sspark.library.extension.toJson
import whiz.tss.sspark.s_spark_android.presentation.assignment.InstructorAssignmentDetailActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_assignment.student.StudentClassAssignmentFragment

class InstructorClassAssignmentFragment: StudentClassAssignmentFragment() {

    companion object {
        fun newInstance(classGroupId: String, startColor: Int) = InstructorClassAssignmentFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putInt("startColor", startColor)
            }
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getLatestAssignments(classGroupId, PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
        }
    }

    override fun initView() {
        super.initView()

        with(binding.vCreateAssignment) {
            visibility = View.VISIBLE
            setOnClickListener {
                // TODO wait create assignment screen
            }
        }
    }

    override fun onNavigateToAssignmentDetail(assignment: Assignment) {
        val intent = Intent(requireContext(), InstructorAssignmentDetailActivity::class.java).apply {
            putExtra("assignment", assignment.toJson())
        }

        resultLauncher.launch(intent)
    }
}