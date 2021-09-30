package whiz.tss.sspark.s_spark_android.presentation.calendar

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.CalendarViewModel
import whiz.sspark.library.extension.toCalendarEventType
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.calendar.CalendarAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityCalendarBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import java.util.*

class CalendarActivity : BaseActivity() {

    private val viewModel: CalendarViewModel by viewModel()

    private lateinit var binding: ActivityCalendarBinding

    private lateinit var currentTerm: Term
    private var dataWrapper: DataWrapperX<Any>? = null
    private var calendars: List<CalendarDTO> = listOf()
    private var calendarInfo: List<CalendarInfoDTO> = listOf()

    private var selectedIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
            initView()

            if (dataWrapper != null) {
                binding.vCalendar.setLatestUpdatedText(dataWrapper)

                updateMonth()
                updateAdapterItem()
            } else {
                viewModel.getCalendar(currentTerm.id)
                viewModel.getCalendarInfo()
            }
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
                calendars.getOrNull(selectedIndex - 1)?.let {
                    selectedIndex -= 1
                    updateMonth()
                    updateAdapterItem()
                }
            },
            onNextMonthClicked = {
                calendars.getOrNull(selectedIndex + 1)?.let {
                    selectedIndex -= 1
                    updateMonth()
                    updateAdapterItem()
                }
            },
            onInfoClicked = {
                            // TODO wait information dialog
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
                initSelectedMonth()
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

        viewModel.calendarInfoErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                showAlertWithOkButton(it)
            }
        }
    }

    private fun initSelectedMonth() {
        val month = Calendar.getInstance().get(Calendar.MONTH)

        if (calendars.isNotEmpty()) {
            for (index in calendars.lastIndex downTo 0) {
                val calendarMonth = calendars[index].month
                if (month >= calendarMonth) {
                    selectedIndex = calendarMonth
                    updateMonth()
                    return
                }
            }
        }
    }

    private fun updateMonth() {
        calendars.getOrNull(selectedIndex)?.let {
            val isHasPreviousItem = calendars.getOrNull(selectedIndex - 1) != null
            val isHasNextItem = calendars.getOrNull(selectedIndex + 1) != null
            val date = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.MONTH, it.month - 1)
                set(Calendar.YEAR, it.year)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            val monthSelection = MonthSelection(
                isShowNextButton = isHasNextItem,
                isShowPreviousButton = isHasPreviousItem,
                date = date
            )

            binding.vCalendar.updateSelectedMonth(monthSelection)
        }
    }

    private fun updateAdapterItem() {
        val items = mutableListOf<CalendarAdapter.CalendarItem>()

        if (calendars.isEmpty()) {
            val title = CalendarAdapter.CalendarItem.NoEvent(
                title = resources.getString(R.string.calendar_no_calendar)
            )

            items.add(title)
        } else {
            calendars.getOrNull(selectedIndex)?.let { calendar ->
                if (calendar.events.isEmpty()) {
                    val title = CalendarAdapter.CalendarItem.NoEvent(
                        title = resources.getString(R.string.calendar_no_event)
                    )

                    items.add(title)
                } else {
                    val tempCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, calendar.year)
                        set(Calendar.MONTH, calendar.month - 1)
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    val entries = generateHighlightDays(calendar, tempCalendar)

                    val calendarItem = CalendarAdapter.CalendarItem.Calendar(
                        month = calendar.month,
                        year = calendar.year,
                        entries = entries,
                        isExamCalendar = false
                    )

                    items.add(calendarItem)

                    calendar.events.forEach {
                        val event = CalendarAdapter.CalendarItem.Event(
                            startDate = it.fromDate,
                            endDate = it.toDate,
                            color = it.colorCode,
                            description = it.name
                        )

                        items.add(event)
                    }
                }
            }
        }

        binding.vCalendar.updateCalendar(items)
    }

    private fun generateHighlightDays(calendar: CalendarDTO, tempCalendar: Calendar): MutableList<CalendarEntry> {
        val highlightDays = mutableListOf<CalendarEntry>()

        val month = calendar.month - 1
        val events = calendar.events.sortedBy { it.fromDate }

        while(tempCalendar.get(Calendar.MONTH) == month) {
            val date = tempCalendar.time
            val existingEvents = events.filter { date >= it.fromDate && date <= it.toDate }

            if(existingEvents.any()) {
                val day = tempCalendar.get(Calendar.DAY_OF_MONTH)

                val prioritizedEvent = existingEvents.maxByOrNull { it.type.toCalendarEventType().type.toIntOrNull() ?: 0 }!!

                val consecutiveDay = highlightDays.singleOrNull { (it.day + it.eventCount) == day && it.title == prioritizedEvent.name}
                if(consecutiveDay == null) {
                    highlightDays.add(CalendarEntry(day, 1, prioritizedEvent.type.toCalendarEventType(), prioritizedEvent.name, prioritizedEvent.colorCode))
                } else {
                    when {
                        prioritizedEvent.type.toCalendarEventType().type == consecutiveDay.type.type -> {
                            val addedEventCount = consecutiveDay.eventCount + 1
                            highlightDays.removeAll { it.day == consecutiveDay.day }
                            highlightDays.add(CalendarEntry(consecutiveDay.day, addedEventCount, consecutiveDay.type, consecutiveDay.title, consecutiveDay.colorCode))
                        }
                        else -> highlightDays.add(CalendarEntry(day, 1, prioritizedEvent.type.toCalendarEventType(), prioritizedEvent.name, prioritizedEvent.colorCode))
                    }
                }
            }

            tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return highlightDays
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedIndex = savedInstanceState.getInt("selectedIndex", -1)
        calendars = savedInstanceState.getString("calendars")?.toObjects(Array<CalendarDTO>::class.java) ?: listOf()
        calendarInfo = savedInstanceState.getString("calendarInfo")?.toObjects(Array<CalendarInfoDTO>::class.java) ?: listOf()
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject<DataWrapperX<Any>>()
        currentTerm = savedInstanceState.getString("currentTerm")?.toObject()!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedIndex", selectedIndex)
        outState.putString("calendars", calendars.toJson())
        outState.putString("calendarInfo", calendarInfo.toJson())
        outState.putString("dataWrapper", dataWrapper?.toJson())
        outState.putString("currentTerm", currentTerm.toJson())
    }
}