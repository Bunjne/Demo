package whiz.sspark.library.view.event.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.Event
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewEventSmallItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toLocalDate

class EventSmallItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewEventSmallItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(event: Event, onEventClicked: (Event) -> Unit) {
        with(event) {

            binding.ivCover.show(coverImageUrl)

            binding.tvTime.text = startedAt.toLocalDate()!!.convertToDateString(
                defaultPattern = DateTimePattern.todayAbbreviatedDateFormatEn,
                dayMonthThPattern = DateTimePattern.todayAbbreviatedDayMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            ).toUpperCase()

            binding.tvName.text = name
            binding.tvVenue.text = venue
        }

        setOnClickListener {
            onEventClicked(event)
        }
    }
}