package whiz.tss.sspark.s_spark_android.presentation.calendar

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.CalendarDTO
import whiz.sspark.library.data.entity.CalendarInfoDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.CalendarViewModel
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityCalendarBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class CalendarActivity : BaseActivity() {

    private val viewModel: CalendarViewModel by viewModel()

    private lateinit var binding: ActivityCalendarBinding

    private lateinit var currentTerm: Term
    private var dataWrapper: DataWrapperX<Any>? = null
    private var calendars: List<CalendarDTO> = listOf()
    private var calendarInfo: List<CalendarInfoDTO> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        } else {
            lifecycleScope.launch {
                profileManager.term.collect {
                    it?.let {
                        currentTerm = it

                        initView()

                        viewModel.getCalendar(currentTerm.id)
                        viewModel.getCalendarInfo()
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.vCalendar.init(
            term = resources.getString(R.string.school_record_term, currentTerm.term.toString(), currentTerm.year.toString()),
            onPreviousMonthClicked = {

            },
            onNextMonthClicked = {

            },
            onRefresh = {
                viewModel.getCalendar(currentTerm.id)
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vCalendar.setSwipeRefreshLayout(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vCalendar.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.calendarResponse.observe(this) {
            it?.let {
                calendars = it
                updateAdapterItem()
            }
        }

        viewModel.calendarInfoResponse.observe(this) {
            it?.let {
                calendarInfo = it
            }
        }
    }

    override fun observeError() {
        viewModel.calendarErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }
    }

    private fun updateAdapterItem() {

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }
}