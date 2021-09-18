package whiz.sspark.library.view.widget.collaboration.class_attendance

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.ClassAttendance
import whiz.sspark.library.data.enum.ClassAttendanceStatus
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewClassAttendanceItemBinding
import whiz.sspark.library.extension.convertToDateString

class ClassAttendanceItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewClassAttendanceItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(attendanceClass: ClassAttendance) {
        with(attendanceClass) {
            binding.tvTitle.text = resources.getString(R.string.class_attendance_checked_item_title_place_holder, name)
            binding.tvStatus.text = status

            when (status) {
                ClassAttendanceStatus.PRESENT.status -> {
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentGreenV500))
                    binding.tvStatus.text = resources.getString(R.string.class_attendance_present)
                }
                ClassAttendanceStatus.LEAVE.status -> {
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentYellowV500))
                    binding.tvStatus.text = resources.getString(R.string.class_attendance_leave)
                }
                ClassAttendanceStatus.LATE.status -> {
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentOrangeV500))
                    binding.tvStatus.text = resources.getString(R.string.class_attendance_item_late)
                }
                else -> {
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentRedV500))
                    binding.tvStatus.text = resources.getString(R.string.class_attendance_absent)
                }
            }

            val checkedDate = checkedAt.convertToDateString(
                defaultPattern = DateTimePattern.attendanceClassDateFormatEn,
                dayMonthThPattern = DateTimePattern.attendanceClassDayMonthFormatTh,
                yearThPattern = DateTimePattern.attendanceClassYearFormatTh
            )

            val checkedTime = checkedAt.convertToDateString(
                defaultPattern = DateTimePattern.attendanceClassTimeFormat
            )

            binding.tvCheckDate.text = resources.getString(R.string.class_attendance_class_check_date_place_holder, checkedDate, checkedTime)
        }
    }
}