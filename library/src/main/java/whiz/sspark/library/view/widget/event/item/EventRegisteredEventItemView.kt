package whiz.sspark.library.view.widget.event.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewEventRegisteredEventItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toLocalDate

class EventRegisteredEventItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewEventRegisteredEventItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(event: EventList,
             onEventClicked: (String, String) -> Unit) {
        with(event) {
            binding.ivPoster.show(imageUrl, R.drawable.ic_download_image_placeholder)
            binding.tvEventLocation.text = location
            binding.tvEventName.text = name
            binding.tvEventDate.text = startAt.toLocalDate()!!.convertToDateString(
                defaultPattern = DateTimePattern.todayAbbreviatedDateFormatEn,
                dayMonthThPattern = DateTimePattern.todayAbbreviatedDayMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            ).uppercase()

            setOnClickListener {
                onEventClicked(id, imageUrl)
            }
        }
    }
}