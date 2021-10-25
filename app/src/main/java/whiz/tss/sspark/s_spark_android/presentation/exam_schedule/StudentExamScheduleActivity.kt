package whiz.tss.sspark.s_spark_android.presentation.exam_schedule

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.CalendarEventType
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.data.viewModel.StudentExamScheduleViewModel
import whiz.sspark.library.extension.*
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.exam_schedule.ExamScheduleAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityStudentExamScheduleBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class StudentExamScheduleActivity : BaseActivity() {

    companion object {
        const val MIDTERM_TAB = 0
        const val FINAL_TAB = 1
    }

    private val viewModel: StudentExamScheduleViewModel by viewModel()

    private lateinit var binding: ActivityStudentExamScheduleBinding

    private val segmentTitles by lazy {
        resources.getStringArray(R.array.exam_schedule_segment).toList()
    }

    private lateinit var currentTerm: Term

    private var dataWrapper: DataWrapperX<Any>? = null
    private var examSchedule: ExamScheduleDTO? = null
    private var selectedTab = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentExamScheduleBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)

            examSchedule = dataWrapper?.data?.toJson()?.toObject<ExamScheduleDTO>()

            initView()
            updateAdapterItem()
        } else {
            lifecycleScope.launch {
                profileManager.term.collect {
                    it?.let {
                        currentTerm = it

                        initView()

                        viewModel.getClassSchedule(currentTerm.id)
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.vExamSchedule.init(
            term = resources.getString(R.string.school_record_term, currentTerm.term.toString(), convertToLocalizeYear(currentTerm.year)),
            segmentTitles = segmentTitles,
            onSegmentClicked = {
                if (selectedTab != it) {
                    selectedTab = it
                    updateAdapterItem()
                }
            },
            onRefresh = {
                viewModel.getClassSchedule(currentTerm.id)
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vExamSchedule.setSwipeRefreshLayout(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vExamSchedule.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.examScheduleResponse.observe(this) {
            it?.let {
                examSchedule = it
                updateAdapterItem()
            }
        }
    }

    override fun observeError() {
        viewModel.examScheduleErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                binding.vExamSchedule.setLatestUpdatedText(getNullDataWrapperX())
                showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem() {
        if (selectedTab == MIDTERM_TAB) {
            val midtermSchedule = examSchedule?.midterm
            val items = convertToAdapterItem(midtermSchedule)
            binding.vExamSchedule.updateItem(items)
        } else {
            val finalSchedule = examSchedule?.final
            val items = convertToAdapterItem(finalSchedule)
            binding.vExamSchedule.updateItem(items)
        }
    }

    private fun convertToAdapterItem(examSchedule: List<ExamScheduleCourseDTO>?): List<ExamScheduleAdapter.Item> {
        val items = mutableListOf<ExamScheduleAdapter.Item>()

        val groupedSchedule = examSchedule?.sortedBy { it.date }?.groupBy { it.date.toLocalDate()!!.convertToDateString(DateTimePattern.monthYearFormat) }

        if (groupedSchedule.isNullOrEmpty()) {
            items.add(ExamScheduleAdapter.Item(noExamTitle = resources.getString(R.string.class_schedule_no_exam)))
        } else {
            groupedSchedule.forEach {
                val monthYearSplit = it.key.split("/")
                val month = monthYearSplit[0].toInt()
                val year = monthYearSplit[1].toInt()

                val monthYearTitle = it.value.first().date.convertToDateString(
                    defaultPattern = DateTimePattern.fullMonthYearFormat,
                    dayMonthThPattern = DateTimePattern.fullMonthFormatTh,
                    yearThPattern = DateTimePattern.generalYear
                )

                items.add(ExamScheduleAdapter.Item(title = monthYearTitle))

                val groupExamByDay = it.value.groupBy { it.date.convertToDateString(DateTimePattern.fullDayNameWithDayFormat) }

                val calendarEntries = groupExamByDay.map {
                    val daySplit = it.key.split(" ")
                    val day = daySplit[1].toIntOrNull() ?: 1

                    CalendarEntry(
                        startDay = day,
                        eventCount = it.value.size,
                        type = CalendarEventType.EXAM
                    )
                }

                val examCalendar = ExamScheduleCalendar(
                    month = month,
                    year = year,
                    entries = calendarEntries,
                    isExamCalendar = true
                )

                items.add(ExamScheduleAdapter.Item(examScheduleCalendar = examCalendar))

                groupExamByDay.forEach {
                    items.add(ExamScheduleAdapter.Item(title = it.key))

                    it.value.forEach {
                        val examScheduleCourse = ExamScheduleCourse(
                            startTime = it.startTime,
                            endTime = it.endTime,
                            code = it.code,
                            name = it.name,
                            room = it.room
                        )

                        items.add(ExamScheduleAdapter.Item(examScheduleCourse = examScheduleCourse))
                    }
                }
            }
        }

        return items.toList()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject()
        currentTerm = savedInstanceState.getString("currentTerm")?.toObject()!!
        selectedTab = savedInstanceState.getInt("selectedTab")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapperX", dataWrapper?.toJson())
        outState.putString("currentTerm", currentTerm.toJson())
        outState.putInt("selectedTab", selectedTab)
    }
}