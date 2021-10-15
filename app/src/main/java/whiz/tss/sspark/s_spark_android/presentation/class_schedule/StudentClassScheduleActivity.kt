package whiz.tss.sspark.s_spark_android.presentation.class_schedule

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.data.viewModel.StudentClassScheduleViewModel
import whiz.sspark.library.extension.*
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.class_schedule.ClassScheduleAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.databinding.ActivityClassScheduleBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import java.util.*

class StudentClassScheduleActivity : BaseActivity() {

    private val viewModel: StudentClassScheduleViewModel by viewModel()

    private lateinit var binding: ActivityClassScheduleBinding
    private lateinit var currentTerm: Term

    private var popupMenu: PopupMenu? = null

    private var dataWrapperX: DataWrapperX<Any>? = null
    private var weeks = listOf<WeekOfYear>()
    private var terms = listOf<Term>()
    private var selectedWeekId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassScheduleBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)

            initView()
            val classSchedules = dataWrapperX?.data?.toJson()?.toObjects(Array<ClassScheduleDTO>::class.java) ?: listOf()
            updateAdapterItem(classSchedules)
            updateSelectedWeek()

            val isTermSelectable = terms.size > 1
            binding.vClassSchedule.initMultipleTerm(isTermSelectable) {
                initPopupMenu(it)
            }
        } else {
            getInitialTerm()

            initView()
            getClassSchedule()
            updateSelectedWeek()
            viewModel.getTerms()
        }
    }

    private fun getInitialTerm() {
        lifecycleScope.launch {
            profileManager.term.collect {
                it?.let {
                    currentTerm = it
                    weeks = getWeeksOfYear()
                    selectedWeekId = getInitialWeek()
                }
            }
        }
    }

    override fun initView() {
        binding.vClassSchedule.init(
            term = resources.getString(R.string.school_record_term, currentTerm.term.toString(), convertToLocalizeYear(currentTerm.year)),
            onTermClicked = {
                popupMenu?.show()
            },
            onPreviousWeekClicked = {
                if (viewModel.viewLoading.value == false) {
                    selectedWeekId -= 1

                    getClassSchedule()
                    updateSelectedWeek()
                }
            },
            onNextWeekClicked = {
                if (viewModel.viewLoading.value == false) {
                    selectedWeekId += 1

                    getClassSchedule()
                    updateSelectedWeek()
                }
            },
            onRefresh = {
                if (viewModel.viewLoading.value == false) {
                    selectedWeekId = getInitialWeek()

                    getClassSchedule()
                    updateSelectedWeek()
                }
            },
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vClassSchedule.setSwipeRefreshLayout(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapperX = it
            binding.vClassSchedule.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.classScheduleResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                updateAdapterItem(it)
            }
        }

        viewModel.termsResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                terms = it

                val isTermSelectable = terms.size > 1
                binding.vClassSchedule.initMultipleTerm(isTermSelectable) {
                    initPopupMenu(it)
                }
            }
        }
    }

    override fun observeError() {
        viewModel.classScheduleErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                updateAdapterItem(listOf())
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.getContentIfNotHandled()?.let {
                updateAdapterItem(listOf())
                binding.vClassSchedule.setLatestUpdatedText(getNullDataWrapperX())
                showAlertWithOkButton(it)
            }
        }

        viewModel.termsErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it) {
                    finish()
                }
            }
        }
    }

    private fun updateAdapterItem(classSchedulesDTO: List<ClassScheduleDTO>) {
        val items = mutableListOf<ClassScheduleAdapter.Item>()
        val dates = weeks.find { it.id == selectedWeekId }?.dates ?: listOf()

        val classScheduleCalendar = ClassScheduleCalendar(
            slots = classSchedulesDTO.map {
                ScheduleSlot(
                    courseCode = it.code,
                    dayNumber = it.day,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    color = it.colorCode1
                )
            },
            scheduleTimes = SSparkApp.scheduleTime,
            dates = dates
        )

        items.add(ClassScheduleAdapter.Item(classScheduleCalendar = classScheduleCalendar))

        if (classSchedulesDTO.isEmpty()) {
            items.add(ClassScheduleAdapter.Item(noClassTitle = resources.getString(R.string.class_schedule_no_class)))
            binding.vClassSchedule.updateSchedule(items)
        } else {
            dates.forEachIndexed { index, date ->
                val day = index + 1
                val courses = classSchedulesDTO.filter { it.day == day }

                if (courses.isNotEmpty()) {
                    val title = date.convertToDateString(DateTimePattern.fullDayNameWithDayFormat)
                    items.add(ClassScheduleAdapter.Item(title = title))

                    courses.forEach {
                        val instructorNames = it.instructors.map { it.fullName }
                        val classScheduleCourse = ClassScheduleCourse(
                            startTime = it.startTime,
                            endTime = it.endTime,
                            code = it.code,
                            name = it.name,
                            color = it.colorCode1,
                            room = it.room,
                            instructorNames = instructorNames
                        )

                        items.add(ClassScheduleAdapter.Item(classScheduleCourse = classScheduleCourse))
                    }
                }
            }

            binding.vClassSchedule.updateSchedule(items)
        }
    }

    private fun getClassSchedule() {
        val currentWeek = weeks.find { it.id == selectedWeekId }?.dates ?: listOf()
        if (currentWeek.isNotEmpty()) {
            viewModel.getClassSchedule(currentTerm.id, currentWeek.first(), currentWeek.last())
        }
    }

    private fun updateSelectedWeek() {
        val selectedWeek = weeks.find { it.id == selectedWeekId }

        selectedWeek?.let {
            val isShowNextPageButton = weeks.find { it.id == selectedWeekId + 1 } != null
            val isShowPreviousPageButton = weeks.find { it.id == selectedWeekId - 1 } != null

            val weekSelection = WeekSelection(
                isShowNextPageButton = isShowNextPageButton,
                isShowPreviousPageButton = isShowPreviousPageButton,
                startDate = selectedWeek.dates.first(),
                endDate = selectedWeek.dates.last()
            )

            binding.vClassSchedule.updateSelectedWeek(weekSelection)
        }
    }

    private fun getInitialWeek(): Int {
        val startedWeekOfYear = currentTerm.startAt.toCalendar().get(Calendar.WEEK_OF_YEAR)
        val endedWeekOfYear = currentTerm.endAt.toCalendar().get(Calendar.WEEK_OF_YEAR)

        val currentWeekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)

        return if (currentWeekOfYear in startedWeekOfYear..endedWeekOfYear) {
            currentWeekOfYear
        } else {
            if (currentWeekOfYear < startedWeekOfYear) {
                startedWeekOfYear
            } else {
                endedWeekOfYear
            }
        }
    }

    private fun getWeeksOfYear(): List<WeekOfYear> {
        val startedWeekOfYear = currentTerm.startAt.toLocalDate()!!.toCalendar().get(Calendar.WEEK_OF_YEAR)
        val endedWeekOfYear = currentTerm.endAt.toLocalDate()!!.toCalendar().get(Calendar.WEEK_OF_YEAR)

        val weeksOfYear: MutableList<WeekOfYear> = mutableListOf()

        for (weekOfYear in startedWeekOfYear..endedWeekOfYear) {
            val dateInWeek: MutableList<Date> = mutableListOf()

            for (index in 1..7) {
                val date = Calendar.getInstance().apply {
                    set(Calendar.WEEK_OF_YEAR, weekOfYear)
                    set(Calendar.DAY_OF_WEEK, index)
                }.time

                dateInWeek.add(date)
            }

            weeksOfYear.add(WeekOfYear(id = weekOfYear, dates = dateInWeek.toList()))
        }

        return weeksOfYear
    }

    private fun initPopupMenu(view: View) {
        popupMenu = PopupMenu(this, view).apply {
            setOnMenuItemClickListener {
                val splitTerm = it.title.split("/")

                val term = splitTerm.getOrNull(0)?.toIntOrNull() ?: 0
                val year = splitTerm.getOrNull(1) ?: ""

                val selectedTerm = terms.find { it.term == term && convertToLocalizeYear(it.year) == year }

                if (selectedTerm != null && selectedTerm != currentTerm) {
                    currentTerm = selectedTerm
                    weeks = getWeeksOfYear()
                    selectedWeekId = getInitialWeek()

                    updateTerm()
                    getClassSchedule()
                    updateSelectedWeek()
                }

                true
            }

            menu.clear()

            terms.forEach {
                val selectAbleTerm = resources.getString(R.string.school_record_term, it.term.toString(), convertToLocalizeYear(it.year))
                menu.add(selectAbleTerm)
            }
        }
    }

    private fun updateTerm() {
        val termTitle = resources.getString(R.string.school_record_term, currentTerm.term.toString(), convertToLocalizeYear(currentTerm.year))
        binding.vClassSchedule.updateTerm(termTitle)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        dataWrapperX = savedInstanceState.getString("dataWrapperX")?.toObject()
        currentTerm = savedInstanceState.getString("currentTerm")?.toObject() ?: Term()
        weeks = savedInstanceState.getString("weeks")?.toObjects(Array<WeekOfYear>::class.java) ?: listOf()
        terms = savedInstanceState.getString("terms")?.toObjects(Array<Term>::class.java) ?: listOf()
        selectedWeekId = savedInstanceState.getInt("selectedWeekId", 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapperX", dataWrapperX?.toJson())
        outState.putString("currentTerm", currentTerm.toJson())
        outState.putString("weeks", weeks.toJson())
        outState.putString("terms", terms.toJson())
        outState.putInt("selectedWeekId", selectedWeekId)
    }
}