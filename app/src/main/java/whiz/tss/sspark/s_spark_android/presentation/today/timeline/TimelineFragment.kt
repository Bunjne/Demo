package whiz.tss.sspark.s_spark_android.presentation.today.timeline

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.enum.TimeLineAuthorityType
import whiz.sspark.library.data.viewModel.TimelineViewModel
import whiz.sspark.library.extension.toLocalDate
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.FragmentTimelineBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import java.util.*

class TimelineFragment : BaseFragment() {

    companion object {
        fun newInstance() = TimelineFragment()
    }

    private val viewModel: TimelineViewModel by viewModel()

    private var updateAqi: OnUpdateAqi? = null
    private var currentDate = Date()

    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateAqi = if (parentFragment != null) {
            parentFragment as OnUpdateAqi
        } else {
            activity as OnUpdateAqi
        }

        initView()
    }

    override fun initView() {
        binding.vTimeline.init(
            onRefresh = {
                viewModel.getTodayDate()
                viewModel.getTimeline(currentDate, 0, true)
            },
            onItemClicked = { linkTo ->
                val uri = Uri.parse(linkTo)
                val authority = uri?.authority
                authority?.let { authority ->
                    when (authority) {
                        TimeLineAuthorityType.CLASS_DETAIL.type -> {
                            with (uri) {
                                val id = getQueryParameter("classGroupId") ?: ""
                                val courseCode = getQueryParameter("courseCode") ?: ""
                                val courseName = getQueryParameter("courseName") ?: ""
                                val sectionNumber = getQueryParameter("sectionNumber") ?: ""
                                val classIconUrl = getQueryParameter("classIconUrl") ?: ""
                                val colorCode = getQueryParameter("Color") ?: ""
                                val allMemberCount = getQueryParameter("allMemberCount")?.toInt() ?: 0

//                                if (colorCode.contains("null") || colorCode.isBlank()) { TODO Waiting for ClassDetail Activity Implementation
//                                    viewModel.getClassGroupV3(id)
//                                } else {
//                                    context?.startActivity<ClassDetailActivity>(
//                                        "id" to id,
//                                        "courseCode" to courseCode,
//                                        "courseName" to courseName,
//                                        "sectionNumber" to sectionNumber,
//                                        "classIconUrl" to classIconUrl,
//                                        "color" to colorCode.toColor()
//                                        "allMemberCount" to allMemberCount
//                                    )
//                                }
                            }
                        }
                        TimeLineAuthorityType.EVENT_DETAIL.type -> {
                            val id = uri.getQueryParameter("Id")?.toLongOrNull() ?: 0L

//                            context?.startActivity<EventDetailActivity>( TODO Waiting for EventDetail Activity Implementation
//                                "eventId" to id,
//                                "viewAsType" to EventViewAsType.ATTENDEE.type
//                            )
                        }
                        TimeLineAuthorityType.ADVISING_APPOINTMENT.type -> {
                            val id = uri.getQueryParameter("periodId")?.toLongOrNull() ?: 0L
                            val title = uri.getQueryParameter("name")

//                            context?.startActivity<AdvisingAppointmentSelectTimeSlotActivity>(
//                                "title" to title,
//                                "appointmentId" to id TODO Waiting for AdvisingAppointment Activity Implementation
//                            )
                        }
                        TimeLineAuthorityType.EXAMINATION.type -> {
                            with (uri) {
                                val term = getQueryParameter("term") ?: ""
                                val academicYear = getQueryParameter("academicYear") ?: ""
                                val examPeriodType = getQueryParameter("examPeriodType") ?: ""

//                                context?.startActivity<ExamActivity>( TODO Waiting for Exam Activity Implementation
//                                    "term" to term.toInt(),
//                                    "academicYear" to academicYear.toInt(),
//                                    "examPeriodType" to examPeriodType
//                                )
                            }
                        }
                        else -> { }
                    }
                }
            },
            onSegmentSelected = { date, differDay ->
                viewModel.getTimeline(date, differDay, false)
            })

        viewModel.getTodayDate()
        viewModel.getTimeline(currentDate, 0, false)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vTimeline.setSwipeRefreshLoading(isLoading)
        })
    }

    override fun observeData() {
        viewModel.timelineResponse.observe(this, Observer {
            it?.let {
                binding.vTimeline.updateTimeline(it)
                binding.vTimeline.updateUniversityEvent(it.alertAnnouncements)

                val backgroundImageUrl = if (SSparkLibrary.isDarkModeEnabled) {
                    it.nightImageUrl
                } else {
                    it.dayImageUrl
                }

                updateAqi?.onUpdateAqi(it.aqiIcon, it.weatherIcon, backgroundImageUrl ?: "", it.aqi , it.aqiColor)
            }
        })

        viewModel.todayDateResponse.observe(this, Observer {
            it?.let {
                currentDate = it.today.toLocalDate()!!
                binding.vTimeline.updateSegment(currentDate)
            }
        })
    }

    override fun observeError() {
        viewModel.timelineErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
                binding.vTimeline.renderNothingView()
            }
        })

        viewModel.todayDateErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            it?.let {
                context?.showAlertWithOkButton(title = it)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        binding.vTimeline.pauseTimer()
    }

    override fun onResume() {
        super.onResume()
        binding.vTimeline.resumeTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnUpdateAqi {
        fun onUpdateAqi(aqiIconUrl: String, weatherIconUrl: String, backgroundImageUrl: String, aqi: Int, color: String)
    }
}