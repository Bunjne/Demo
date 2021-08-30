package whiz.tss.sspark.s_spark_android.presentation.today.timeline

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.TimelineViewModel
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import java.util.*

class TimelineFragment : BaseFragment() {

    companion object {
        fun newInstance() = TimelineFragment().apply {

        }
    }

    private val viewModel: TimelineViewModel by viewModel()

    private var updateAqi: OnUpdateAqi? = null
    private var currentDate = Date()

    private val loadingDialog by lazy {
//        activity?.indeterminateProgressDialog(resources.getString(R.string.loading_dialog_title)) {
//            setCancelable(false) TODO Insert undeprecated progress dialog here
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateAqi = activity as OnUpdateAqi
    }

    override fun initView() {
        vTimeline.init(
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

                                if (colorCode.contains("null") || colorCode.isBlank()) {
                                    viewModel.getClassGroupV3(id)
                                } else {
                                    context?.startActivity<ClassDetailActivity>(
                                        "id" to id,
                                        "courseCode" to courseCode,
                                        "courseName" to courseName,
                                        "sectionNumber" to sectionNumber,
                                        "classIconUrl" to classIconUrl,
                                        "color" to Color.parseColor(colorCode),
                                        "allMemberCount" to allMemberCount
                                    )
                                }
                            }
                        }
                        TimeLineAuthorityType.EVENT_DETAIL.type -> {
                            val id = uri.getQueryParameter("Id")?.toLongOrNull() ?: 0L

                            context?.startActivity<EventDetailActivity>(
                                "eventId" to id,
                                "viewAsType" to EventViewAsType.ATTENDEE.type
                            )
                        }
                        TimeLineAuthorityType.ADVISING_APPOINTMENT.type -> {
                            val id = uri.getQueryParameter("periodId")?.toLongOrNull() ?: 0L
                            val title = uri.getQueryParameter("name")

                            context?.startActivity<AdvisingAppointmentSelectTimeSlotActivity>(
                                "title" to title,
                                "appointmentId" to id
                            )
                        }
                        TimeLineAuthorityType.EXAMINATION.type -> {
                            with (uri) {
                                val term = getQueryParameter("term") ?: ""
                                val academicYear = getQueryParameter("academicYear") ?: ""
                                val examPeriodType = getQueryParameter("examPeriodType") ?: ""

                                context?.startActivity<ExamActivity>(
                                    "term" to term.toInt(),
                                    "academicYear" to academicYear.toInt(),
                                    "examPeriodType" to examPeriodType
                                )
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
        TODO("Not yet implemented")
    }

    override fun observeData() {
        TODO("Not yet implemented")
    }

    override fun observeError() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        super.onPause()
        vTimeline.pauseTimer()
    }

    override fun onResume() {
        super.onResume()
        vTimeline.resumeTimer()
    }

    interface OnUpdateAqi {
        fun onUpdateAqi(aqiIconUrl: String, weatherIconUrl: String, backgroundImageUrl: String, aqi: Int, color: String)
    }
}