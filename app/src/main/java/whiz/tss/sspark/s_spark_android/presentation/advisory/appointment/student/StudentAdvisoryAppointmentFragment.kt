package whiz.tss.sspark.s_spark_android.presentation.advisory.appointment.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.AdvisoryAppointment
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.enum.AdvisingStatus
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.data.viewModel.StudentAdvisoryAppointmentViewModel
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.advisory.student.StudentAdvisoryAppointmentAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentAdvisoryAppointmentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.advisory.AdvisoryActivity
import java.util.*

class StudentAdvisoryAppointmentFragment: BaseFragment(), AdvisoryActivity.OnSegmentChangedListener {

    companion object {
        const val PENDING_APPOINTMENT = 0
        const val PAST_APPOINTMENT = 1

        fun newInstance() = StudentAdvisoryAppointmentFragment()
    }

    private val viewModel: StudentAdvisoryAppointmentViewModel by viewModel()

    private var _binding: FragmentStudentAdvisoryAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentTerm: Term
    private var selectedSegmentId = PENDING_APPOINTMENT

    private val advisoryAppointments = mutableListOf<AdvisoryAppointment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStudentAdvisoryAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileManager.term.collect {
                it?.let {
                    currentTerm = it
                    initView()

                    getAdvisorySlot()
                }
            }
        }
    }

    override fun initView() {
        binding.vAdvisoryAppointment.init(
            onRefresh = {
                getAdvisorySlot()
            },
            onSelectStatusClicked = { view, slot ->
                PopupMenu(requireContext(), view).run {
                    setOnMenuItemClickListener {

                        if (slot.status == AdvisingStatus.AVAILABLE.status) {
                            viewModel.reserveAdvisorySlot(slot.id)
                        } else {
                            viewModel.cancelAdvisorySlot(slot.id)
                        }

                        true
                    }

                    if (slot.status == AdvisingStatus.AVAILABLE.status) {
                        menu.add(resources.getString(R.string.advisory_appointment_reserved_status))
                    } else {
                        menu.add(resources.getString(R.string.advisory_appointment_cancelled_status))
                    }

                    show()
                }
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAdvisoryAppointment.setSwipeRefreshLayout(isLoading)
        }

        viewModel.actionLoading.observe(this) { isLoading ->
            if (isLoading == true) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    override fun observeData() {
        viewModel.advisoryAppointmentSlotResponses.observe(this) {
            it?.let {
                with(advisoryAppointments) {
                    clear()
                    addAll(it)
                }

                renderData()
            }
        }

        listOf(viewModel.reserveAdvisoryAppointmentResponse,
            viewModel.cancelAdvisoryAppointmentResponse).forEach {
                it.observe(this) {
                    it?.let {
                        getAdvisorySlot()
                    }
                }
        }
    }

    override fun observeError() {
        listOf(viewModel.advisoryAppointmentSlotErrorResponse,
            viewModel.reserveAdvisoryAppointmentErrorResponse,
            viewModel.cancelAdvisoryAppointmentErrorResponse).forEach {
                it.observe(this) {
                    it?.let {
                        showApiResponseXAlert(requireContext(), it)
                    }
                }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }

    override fun onSegmentChanged(currentSegmentId: Int) {
        if (currentSegmentId != selectedSegmentId) {
            selectedSegmentId = currentSegmentId

            getAdvisorySlot()
        }
    }

    private fun getAdvisorySlot() {
        when (selectedSegmentId) {
            PENDING_APPOINTMENT -> {
                viewModel.getPendingAdvisorySlot(currentTerm.id)
            }
            PAST_APPOINTMENT -> {
                viewModel.getHistoryAdvisorySlot(currentTerm.id)
            }
        }
    }

    private fun renderData() {
        val advisorySlotItems = mutableListOf<StudentAdvisoryAppointmentAdapter.AppointmentItem>()

        advisoryAppointments.forEach {
            val title = it.date.convertToDateString(
                defaultPattern = DateTimePattern.shortDayAndMonthFormatEn,
                dayMonthThPattern = DateTimePattern.shortDayAndMonthFormatTh,
                yearThPattern = DateTimePattern.twoPositionYear
            )

            advisorySlotItems.add(StudentAdvisoryAppointmentAdapter.AppointmentItem.Title(title))

            val appointmentSlotItem = it.slot.map { StudentAdvisoryAppointmentAdapter.AppointmentItem.AppointmentSlot(it) }
            advisorySlotItems.addAll(appointmentSlotItem)
        }

        binding.vAdvisoryAppointment.updateItem(advisorySlotItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}