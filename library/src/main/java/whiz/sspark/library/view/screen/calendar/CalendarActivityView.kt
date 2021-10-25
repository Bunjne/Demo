package whiz.sspark.library.view.screen.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.databinding.ViewCalendarActivityBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showViewStateX
import whiz.sspark.library.view.general.custom_divider.CustomDividerItemDecoration
import whiz.sspark.library.view.widget.calendar.CalendarAdapter
import java.lang.Exception

class CalendarActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewCalendarActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(term: String,
             onPreviousMonthClicked: () -> Unit,
             onNextMonthClicked: () -> Unit,
             onInfoClicked: () -> Unit,
             onRefresh: () -> Unit) {
        binding.tvTerm.text = term

        with(binding.icMoreInfo) {
            show(R.drawable.ic_info)
            setOnClickListener {
                onInfoClicked()
            }
        }

        with(binding.rvCalendar) {
            addItemDecoration(
                CustomDividerItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = CalendarAdapter.EVENT_VIEW_TYPE
                )
            )

            this.layoutManager = LinearLayoutManager(context)
            adapter = CalendarAdapter(
                onPreviousMonthClicked = onPreviousMonthClicked,
                onNextMonthClicked = onNextMonthClicked
            )
        }

        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun setLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.tvLatestUpdated.showViewStateX(data)
    }

    fun updateCalendar(items: List<CalendarAdapter.CalendarItem>) {
        (binding.rvCalendar.adapter as? CalendarAdapter)?.submitList(items) {
            try {
                binding.rvCalendar.scrollToPosition(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}