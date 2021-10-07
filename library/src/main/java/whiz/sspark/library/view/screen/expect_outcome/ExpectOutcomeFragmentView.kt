package whiz.sspark.library.view.screen.expect_outcome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.databinding.ViewExpectOutcomeFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.view.widget.school_record.expect_outcome.ExpectOutcomeAdapter

class ExpectOutcomeFragmentView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewExpectOutcomeFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String,
             subTitle: String,
             credit: Int,
             advisee: Advisee?,
             onCloseClicked: () -> Unit,
             onInfoClicked: () -> Unit,
             onRefresh: () -> Unit) {
        if (advisee != null) {
            binding.vAdviseeProfile.init(advisee)
            binding.vAdviseeProfile.visibility = View.VISIBLE
        }

        binding.ivInfo.show(R.drawable.ic_info)
        binding.ivClose.show(R.drawable.ic_close_bottom_sheet_dialog)

        binding.tvTitle.text = title
        binding.tvCredit.text = resources.getString(R.string.general_credit, credit.toString())
        binding.tvSubTitle.text = subTitle

        with(binding.rvExpectOutcome) {
            layoutManager = LinearLayoutManager(context)
            adapter = ExpectOutcomeAdapter(context)
        }

        binding.srlContainer.setOnRefreshListener {
            onRefresh()
        }

        binding.ivInfo.setOnClickListener {
            onInfoClicked()
        }

        binding.ivClose.setOnClickListener {
            onCloseClicked()
        }
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun updateItem(items: List<ExpectOutcomeAdapter.Item>) {
        (binding.rvExpectOutcome.adapter as? ExpectOutcomeAdapter)?.submitList(items)
    }
}
