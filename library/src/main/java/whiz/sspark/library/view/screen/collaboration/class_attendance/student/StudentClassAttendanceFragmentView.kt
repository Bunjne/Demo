package whiz.sspark.library.view.screen.collaboration.class_attendance.student

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.Attendance
import whiz.sspark.library.databinding.ViewStudentClassAttendanceFragmentBinding
import whiz.sspark.library.view.widget.collaboration.class_attendance.ClassAttendanceAdapter

class StudentClassAttendanceFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewStudentClassAttendanceFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit) {
        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }

        binding.vSummary.init()
    }

    fun renderAttendance(attendance: Attendance) {
        binding.vSummary.renderSummaryAttendance(attendance)

        with (binding.rvAttendance) {
            layoutManager = LinearLayoutManager(context)
            adapter = ClassAttendanceAdapter(
                context = context,
                attendanceClasses = attendance.classes
            )
        }

        if (attendance.classes.isNotEmpty()) {
            setNoAttendanceVisibility(false)
        } else {
            setNoAttendanceVisibility(true)
        }
    }

    fun setNoAttendanceVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.tvNoRecords.visibility = View.VISIBLE
        } else {
            binding.tvNoRecords.visibility = View.INVISIBLE
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading
    }
}