package whiz.sspark.library.view.screen.advisee_list

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewAdviseeListActivityBinding
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.widget.advisee_list.AdviseeListAdapter
import whiz.sspark.library.view.widget.advisee_list.AdviseeListHeaderAdapter

class AdviseeListActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAdviseeListActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var headerAdapter: AdviseeListHeaderAdapter? = null
    private var adviseeAdapter: AdviseeListAdapter? = null

    fun init(title: String,
             segmentTitles: List<String>,
             onAdviseeClicked: (Advisee) -> Unit,
             onSegmentClicked: (Int) -> Unit,
             onTextChanged: (String) -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTitle.text = title

        headerAdapter = AdviseeListHeaderAdapter(
            segmentTitles = segmentTitles,
            onSegmentClicked = onSegmentClicked,
            onTextChanged = onTextChanged
        )

        adviseeAdapter = AdviseeListAdapter(onAdviseeClicked)

        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()

        val concatAdapter = ConcatAdapter(config, headerAdapter, adviseeAdapter)

        with(binding.rvAdvisee) {
            layoutManager = LinearLayoutManager(context)
            adapter = concatAdapter

            post {
                adviseeAdapter?.notifyOnSizeChange(binding.rvAdvisee.height)
            }
        }

        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateItem(advisees: List<AdviseeListAdapter.AdviseeListItem>) {
        adviseeAdapter?.submitList(advisees)
    }

    fun updateAdapterHeader(isPrimaryHighSchool: Boolean) {
        val headerItem = if (isPrimaryHighSchool) {
            listOf(
                AdviseeListHeaderAdapter.AdviseeListHeaderItem.Search
            )
        } else {
            listOf(
                AdviseeListHeaderAdapter.AdviseeListHeaderItem.Search,
                AdviseeListHeaderAdapter.AdviseeListHeaderItem.Segment
            )
        }

        headerAdapter?.submitList(headerItem)
    }

    fun setInitialHeader(selectedTab: Int, text: String) {
        headerAdapter?.setInitialAdapter(selectedTab, text)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        binding.rvAdvisee.post {
            adviseeAdapter?.notifyOnSizeChange(binding.rvAdvisee.height)
        }
    }
}