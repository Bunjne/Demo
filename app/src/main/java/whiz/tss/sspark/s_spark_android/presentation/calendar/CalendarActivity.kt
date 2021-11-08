package whiz.tss.sspark.s_spark_android.presentation.calendar

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.CalendarViewModel
import whiz.sspark.library.extension.*
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.calendar.CalendarAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityCalendarBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.calendar.info_dialog.InformationDialog
import java.util.*

class CalendarActivity : BaseActivity() {

    companion object {
        const val CALENDAR_INFO_DIALOG = "CalendarInfoDialog"
    }

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
        binding.vProfile.init(lifecycle)
        binding.vCalendar.init(
            term = resources.getString(R.string.school_record_term, currentTerm.term.toString(), convertToLocalizeYear(currentTerm.year)),
            onPreviousMonthClicked = {
                if (viewModel.viewLoading.value == false) {
                    val previousIndex = selectedIndex - 1
                    val calendar = calendars.getOrNull(previousIndex)
                    if (calendar != null) {
                        selectedIndex -= 1
                        updateAdapterItem()
                    }
                }
            },
            onNextMonthClicked = {
                if (viewModel.viewLoading.value == false) {
                    val nextIndex = selectedIndex + 1
                    val calendar = calendars.getOrNull(nextIndex)

                    if (calendar != null) {
                        selectedIndex += 1
                        updateAdapterItem()
                    }
                }
            },
            onInfoClicked = {
                if (viewModel.viewLoading.value == false) {
                    val isShowing = supportFragmentManager.findFragmentByTag(CALENDAR_INFO_DIALOG) != null

                    if (!isShowing) {
                        val informationItems = calendarInfo.map {
                            CalendarInformationIndex(
                                _color = it.colorCode,
                                description = it.name
                            )
                        }.toInformationItems()

                        InformationDialog.newInstance(
                            headerText = resources.getString(R.string.information_dialog_calendar_information_color_header),
                            informationItems = informationItems
                        ).show(supportFragmentManager, CALENDAR_INFO_DIALOG)
                    }
                }
            },
            onRefresh = {
                viewModel.getCalendar(currentTerm.id)
                viewModel.getCalendarInfo()
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
            it.getContentIfNotHandled()?.let {
                calendars = it
                selectedIndex = getInitialMonth()
                updateAdapterItem()
            }
        }

        viewModel.calendarInfoResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                calendarInfo = it
            }
        }
    }

    override fun observeError() {
        viewModel.calendarErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.calendarInfoErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.getContentIfNotHandled()?.let {
                binding.vCalendar.setLatestUpdatedText(getNullDataWrapperX())
                showAlertWithOkButton(it)
            }
        }
    }

    private fun getInitialMonth(): Int {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

        if (calendars.isNotEmpty()) {
            var index = 0

            while (calendars.getOrNull(index)?.month ?: 12 < currentMonth ) {
                if (index < calendars.lastIndex) {
                    index++
                } else {
                    break
                }
            }

            return index
        }

        return -1
    }

    private fun updateAdapterItem() {
        val items = mutableListOf<CalendarAdapter.CalendarItem>()

        if (calendars.isEmpty()) {
            val title = CalendarAdapter.CalendarItem.NoEvent(
                title = resources.getString(R.string.calendar_no_calendar)
            )

            items.add(title)
        } else {
            val calendar = calendars.getOrNull(selectedIndex)
            if (calendar!= null) {
                val isHasPreviousItem = calendars.getOrNull(selectedIndex - 1) != null
                val isHasNextItem = calendars.getOrNull(selectedIndex + 1) != null

                val initialCalendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.MONTH, calendar.month - 1)
                    set(Calendar.YEAR, calendar.year)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val monthSelection = CalendarAdapter.CalendarItem.MonthSelection(
                    isShowNextButton = isHasNextItem,
                    isShowPreviousButton = isHasPreviousItem,
                    date = initialCalendar.time
                )

                items.add(monthSelection)

                val entries = generateHighlightDays(calendar, initialCalendar)

                val calendarItem = CalendarAdapter.CalendarItem.Calendar(
                    month = calendar.month,
                    year = calendar.year,
                    entries = entries,
                    isExamCalendar = false
                )

                items.add(calendarItem)

                if (calendar.events.isEmpty()) {
                    val title = CalendarAdapter.CalendarItem.NoEvent(
                        title = resources.getString(R.string.calendar_no_event)
                    )

                    items.add(title)
                } else {
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
            } else {
                val title = CalendarAdapter.CalendarItem.NoEvent(
                    title = resources.getString(R.string.calendar_no_calendar)
                )

                items.add(title)
            }
        }

        binding.vCalendar.updateCalendar(items)
    }

    private fun generateHighlightDays(calendar: CalendarDTO, initialCalendar: Calendar): List<CalendarEntry> {
        if (calendar.events.isEmpty()) {
            return listOf()
        } else {
            val highlightDays = mutableListOf<CalendarEntry>()

            val month = calendar.month - 1
            val events = calendar.events.sortedBy { it.fromDate.toLocalDate() }

            while(initialCalendar.get(Calendar.MONTH) == month) {
                val existingEvents = events.filter {
                    it.fromDate.toLocalDate()!!.toCalendar(true) <= initialCalendar && it.toDate.toLocalDate()!!.toCalendar(true) >= initialCalendar
                }

                if (existingEvents.any()) {
                    val day = initialCalendar.get(Calendar.DAY_OF_MONTH)

                    val prioritizedEvent = existingEvents.maxByOrNull { it.type.toCalendarEventType().value }!!

                    val consecutiveDay = highlightDays.singleOrNull { (it.startDay + it.eventCount) == day && it.title == prioritizedEvent.name && it.type == prioritizedEvent.type.toCalendarEventType() }
                    if (consecutiveDay == null) {
                        highlightDays.add(CalendarEntry(day, 1, prioritizedEvent.type.toCalendarEventType(), prioritizedEvent.name, prioritizedEvent.colorCode))
                    } else {
                        val addedEventCount = consecutiveDay.eventCount + 1
                        highlightDays.removeAll { it.startDay == consecutiveDay.startDay }
                        highlightDays.add(CalendarEntry(consecutiveDay.startDay, addedEventCount, consecutiveDay.type, consecutiveDay.title, consecutiveDay.colorCode))
                    }
                }

                initialCalendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            return highlightDays
        }
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