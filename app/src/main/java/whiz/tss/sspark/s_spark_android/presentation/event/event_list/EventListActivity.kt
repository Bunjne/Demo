package whiz.tss.sspark.s_spark_android.presentation.event.event_list

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.viewModel.EventListViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.ActivityEventListBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class EventListActivity: BaseActivity() {

    private val viewModel: EventListViewModel by viewModel()

    private lateinit var binding: ActivityEventListBinding

    private val title by lazy {
        intent?.getStringExtra("title") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        viewModel.getEvent()
        viewModel.getHighlightEvents()
    }

    override fun initView() {
        binding.vProfile.init(lifecycle)
        binding.vEventList.init(
            title = title,
            onEventClicked = { id, imageUrl ->
                //TODO wait implement event detail
            },
            onRegisteredEventClicked = {
                //TODO wait implement event history
            },
            onRefresh = {
                viewModel.getEvent()
                viewModel.getHighlightEvents()
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vEventList.setSwipeRefreshLayout(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            binding.vEventList.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.eventResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                binding.vEventList.updateEventAdapter(it)
            }
        }

        viewModel.highlightEventResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                binding.vEventList.updateHighlightEventAdapter(it)
            }
        }
    }

    override fun observeError() {
        viewModel.eventErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.highlightEventErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showAlertWithOkButton(it)
            }
        }
    }
}