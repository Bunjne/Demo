package whiz.sspark.library.view.widget.event.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.databinding.ViewEventRegisteredEventItemBinding

class EventRegisteredEventItemView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewEventRegisteredEventItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(event: EventList) {

    }
}