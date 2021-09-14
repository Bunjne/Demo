package whiz.sspark.library.view.widget.collaboration.class_attendance

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Attendance
import whiz.sspark.library.databinding.ViewClassAttendanceInfoSummaryBinding

class ClassAttendanceInfoSummaryView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassAttendanceInfoSummaryBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(attendance: Attendance) {
        val valueColorPairs = mutableListOf<Pair<Double, Int>>()

        with(attendance) {

            if (presentPercent > 0) {
                valueColorPairs.add(presentPercent to ContextCompat.getColor(context, R.color.accentGreenV500))
            }

            if (leavePercent > 0) {
                valueColorPairs.add(leavePercent to ContextCompat.getColor(context, R.color.accentYellowV500))
            }

            if (latePercent > 0) {
                valueColorPairs.add(latePercent to ContextCompat.getColor(context, R.color.accentOrangeV500))
            }

            if (absentPercent > 0) {
                valueColorPairs.add(absentPercent to ContextCompat.getColor(context, R.color.accentRedV500))
            }

            val classCount = classes.size
            binding.tvAllClassCount.text = resources.getString(R.string.class_attendance_valid_class_count, classCount)

            binding.vClassProportion.post {
                binding.vClassProportion.init(valueColorPairs)
            }

            binding.tvPresentPercent.text = "${presentPercent.toInt()}%"
            binding.tvLatePercent.text = "${latePercent.toInt()}%"
            binding.tvAbsentPercent.text = "${absentPercent.toInt()}%"
            binding.tvLeavePercent.text = "${leavePercent.toInt()}%"
        }
    }
}