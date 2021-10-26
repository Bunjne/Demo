package whiz.tss.sspark.s_spark_android.presentation.event.event_registered

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.data.entity.EventRegisteredDTO
import whiz.sspark.library.data.enum.EventType
import whiz.sspark.library.data.viewModel.EventRegisteredViewModel
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.event.EventRegisteredAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityEventRegisteredBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class EventRegisteredActivity : BaseActivity() {

    private val viewModel: EventRegisteredViewModel by viewModel()

    private lateinit var binding: ActivityEventRegisteredBinding

    private val items = mutableListOf<EventRegisteredAdapter.EventRegisteredAdapterViewType>()

    private var currentSegment = -1
    private var savedFragment = -1
    private var segmentType = EventType.UPCOMING.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventRegisteredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        initView()

        viewModel.getRegisteredEvents()
    }

    override fun initView() {
        binding.vEventRegistered.init(
            events = items,
            segmentTitles = resources.getStringArray(R.array.event_registered_segment).toList(),
            title = resources.getString(R.string.event_registered_title),
            onTabClicked = {
                renderSelectedTab(it)
            },
            onRefresh = {
                viewModel.getRegisteredEvents()
            },
            onEventClicked = { id, imageUrl ->
                //TODO wait for the event detail page
            }
        )

        if (savedFragment != -1) {
            binding.vEventRegistered.setSelectedTab(savedFragment)
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vEventRegistered.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            binding.vEventRegistered.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.eventResponse.observe(this) {
            it?.let {
                val transformedRegisteredEvents = transformData(it, segmentType)
                binding.vEventRegistered.renderEvents(items, transformedRegisteredEvents)
            }
        }
    }

    override fun observeError() {
        viewModel.eventErrorResponse.observe(this) {
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedFragment = savedInstanceState.getInt("savedFragment", -1)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("savedFragment", currentSegment)
        viewModelStore.clear()
    }

    private fun renderSelectedTab(tab: Int) {
        currentSegment = tab
        segmentType = when (currentSegment) {
            0 -> EventType.UPCOMING.type
            else -> EventType.PAST.type
        }

        viewModel.eventResponse.value?.let {
            val transformedRegisteredEvents = transformData(it, segmentType)
            binding.vEventRegistered.renderEvents(items, transformedRegisteredEvents)
        }
    }

    private fun transformData(events: EventRegisteredDTO, eventType: String): List<EventRegisteredAdapter.EventRegisteredAdapterViewType> {
        val transformedEvents = mutableListOf<EventRegisteredAdapter.EventRegisteredAdapterViewType>()

        when (eventType) {
            EventType.UPCOMING.type -> {
                transformedEvents.addAll(
                    events.upcomingEvents.map { event ->
                        EventRegisteredAdapter.EventRegisteredAdapterViewType.Event(event.convertToEventListItem())
                    }
                )
            }
            EventType.PAST.type -> {
                events.historicalEvents.map { eventGroup ->
                    transformedEvents.add(
                        EventRegisteredAdapter.EventRegisteredAdapterViewType.Header(resources.getString(R.string.school_record_term, eventGroup.term.toString(), convertToLocalizeYear(eventGroup.year)))
                    )

                    transformedEvents.addAll(
                        eventGroup.events.map { event ->
                            EventRegisteredAdapter.EventRegisteredAdapterViewType.Event(event.convertToEventListItem())
                        }
                    )
                }
            }
        }

        return transformedEvents
    }
}