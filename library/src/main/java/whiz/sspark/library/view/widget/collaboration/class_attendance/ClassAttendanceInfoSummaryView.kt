package whiz.sspark.library.view.widget.collaboration.class_attendance

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Attendance
import whiz.sspark.library.data.entity.AttendanceSummary
import whiz.sspark.library.databinding.ViewClassAttendanceInfoSummaryBinding

class ClassAttendanceInfoSummaryView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassAttendanceInfoSummaryBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init() {
        binding.tvAllClassCount.text = resources.getString(R.string.class_attendance_valid_class_count, 0)

        binding.vClassProportion.post {
            binding.vClassProportion.init(listOf())
        }

        binding.tvPresentPercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, 0)
        binding.tvLatePercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, 0)
        binding.tvAbsentPercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, 0)
        binding.tvLeavePercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, 0)
    }

    fun renderSummaryAttendance(attendance: AttendanceSummary) {
        with(attendance) {
            val valueColorPairs = mutableListOf<Pair<Double, Int>>()

            if (checkCount > 0) {
                valueColorPairs.add(checkCount to ContextCompat.getColor(context, R.color.accentGreenV500))
            }

            if (leaveCount > 0) {
                valueColorPairs.add(leaveCount to ContextCompat.getColor(context, R.color.accentYellowV500))
            }

            if (lateCount > 0) {
                valueColorPairs.add(lateCount to ContextCompat.getColor(context, R.color.accentOrangeV500))
            }

            if (absentCount > 0) {
                valueColorPairs.add(absentCount to ContextCompat.getColor(context, R.color.accentRedV500))
            }

            binding.tvAllClassCount.text = resources.getString(R.string.class_attendance_valid_class_count, totalAttendance)

            binding.vClassProportion.post {
                binding.vClassProportion.init(valueColorPairs)
            }

            binding.tvPresentPercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, checkCount.toInt())
            binding.tvLatePercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, lateCount.toInt())
            binding.tvAbsentPercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, absentCount.toInt())
            binding.tvLeavePercent.text = resources.getString(R.string.class_attendance_percentage_place_holder, leaveCount.toInt())
        }
    }
}