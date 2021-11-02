package whiz.tss.sspark.s_spark_android.presentation.assignment

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.static.PagingConfiguration
import whiz.sspark.library.extension.toJson

class InstructorAssignmentActivity : AssignmentActivity() {

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getLatestAssignments(currentTerm.id, PagingConfiguration.INITIAL_PAGE, PagingConfiguration.PAGE_SIZE)
        }
    }

    override fun onNavigateToAssignmentDetail(assignment: Assignment) {
        val intent = Intent(this, InstructorAssignmentDetailActivity::class.java).apply {
            putExtra("assignment", assignment.toJson())
        }

        resultLauncher.launch(intent)
    }
}