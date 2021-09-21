package whiz.sspark.library.view.widget.notification_inbox

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Inbox
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewNotificationInboxItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show

class NotificationInboxItemView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewNotificationInboxItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(inbox: Inbox) {
        binding.ivArrow.show(R.drawable.ic_arrow_right)

        with(inbox) {
            binding.tvCreatedAt.text = resources.getString(R.string.notification_inbox_created_at, date.convertToDateString(DateTimePattern.generalTime))
            binding.tvDetail.text = detail
            binding.tvTitle.text = title

            if (isRead) {
                binding.cvUnread.visibility = View.INVISIBLE
            } else {
                binding.cvUnread.visibility = View.VISIBLE
            }
        }
    }
}