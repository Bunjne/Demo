package whiz.sspark.library.view.event.item

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import whiz.sspark.library.data.entity.Event
import whiz.sspark.library.databinding.ViewEventSmallItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showBlurImage
import whiz.sspark.library.extension.toLocalDate
import whiz.sspark.library.extension.toTodayAbbreviatedDateFormat
import java.util.*

class EventSmallItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewEventSmallItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(event: Event, onEventClicked: (Event) -> Unit) {
        with(event) {

//            binding.ivCardBackground.showBlurImage(coverImageUrl, 16)
            binding.ivCover.show(coverImageUrl)

            binding.tvTime.text = startedAt.toLocalDate()!!.toTodayAbbreviatedDateFormat().toUpperCase()

            binding.tvName.text = name
            binding.tvVenue.text = venue
        }

        setOnClickListener {
            onEventClicked(event)
        }
    }

    fun setGradientDrawable(drawable: Int) {
//        binding.vGradient.background = ContextCompat.getDrawable(context, drawable)
    }

    fun setTitleColor(color: Int) {
        binding.tvName.setTextColor(color)
    }
}