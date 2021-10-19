package whiz.sspark.library.view.widget.advisory.appointment.student

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.AdvisorySlot
import whiz.sspark.library.data.enum.AdvisingStatus
import whiz.sspark.library.databinding.ViewStudentAdvisoryAppointmentSlotItemBinding
import whiz.sspark.library.extension.convertToTime
import whiz.sspark.library.extension.show

class StudentAdvisoryAppointmentSlotItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewStudentAdvisoryAppointmentSlotItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(slot: AdvisorySlot,
             onSelectStatusClicked: (View, AdvisorySlot) -> Unit) {
        with(slot) {
            binding.tvStartTime.text = startAt.convertToTime()
            binding.tvEndTime.text = resources.getString(R.string.advisory_appointment_end_time_place_holder, endAt.convertToTime())

            binding.ivSelectStatus.show(R.drawable.ic_dropdown)

            when (status) {
                AdvisingStatus.AVAILABLE.status -> {
                    binding.tvStatus.text = resources.getString(R.string.advisory_appointment_available_status)
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentGreenV500))
                    binding.ivSelectStatus.visibility = View.VISIBLE

                    binding.cvAppointmentStatus.setOnClickListener {
                        onSelectStatusClicked(binding.cvAppointmentStatus, this)
                    }
                }
                AdvisingStatus.RESERVED.status -> {
                    binding.tvStatus.text = resources.getString(R.string.advisory_appointment_reserved_status)
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentBlueV300))
                    binding.ivSelectStatus.visibility = View.VISIBLE

                    binding.cvAppointmentStatus.setOnClickListener {
                        onSelectStatusClicked(binding.cvAppointmentStatus, this)
                    }
                }
                AdvisingStatus.CANCELLED.status -> {
                    binding.tvStatus.text = resources.getString(R.string.advisory_appointment_cancelled_status)
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentRedV300))
                    binding.ivSelectStatus.visibility = View.GONE

                    binding.cvAppointmentStatus.setOnClickListener { }
                }
                AdvisingStatus.COMPLETED.status -> {
                    binding.tvStatus.text = resources.getString(R.string.advisory_appointment_completed_status)
                    binding.cvStatus.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accentGrayV600))
                    binding.ivSelectStatus.visibility = View.GONE

                    binding.cvAppointmentStatus.setOnClickListener { }
                }
            }
        }
    }
}