package whiz.sspark.library.view.screen.advisory.appointment.student

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.AdvisorySlot
import whiz.sspark.library.databinding.ViewStudentAdvisoryAppointmentFragmentBinding
import whiz.sspark.library.view.widget.advisory.appointment.student.StudentAdvisoryAppointmentAdapter

class StudentAdvisoryAppointmentFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewStudentAdvisoryAppointmentFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onRefresh: () -> Unit,
             onSelectStatusClicked: (View, AdvisorySlot) -> Unit) {
        binding.srlAdvisorySlot.setOnRefreshListener(onRefresh)

        with(binding.rvAdvisorySlot) {
            layoutManager = LinearLayoutManager(context)
            adapter = StudentAdvisoryAppointmentAdapter(onSelectStatusClicked)
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlAdvisorySlot.isRefreshing = isLoading == true
    }

    fun updateItem(items: List<StudentAdvisoryAppointmentAdapter.AppointmentItem>) {
        (binding.rvAdvisorySlot.adapter as? StudentAdvisoryAppointmentAdapter)?.submitList(items)

        if (items.isEmpty()) {
            binding.tvNoAdvisorySlot.visibility = View.VISIBLE
        } else {
            binding.tvNoAdvisorySlot.visibility = View.GONE
        }
    }
}