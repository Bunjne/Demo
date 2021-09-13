package whiz.sspark.library.view.widget.today.timeline

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewTimelineUniversityAnnouncementBinding
import whiz.sspark.library.extension.show

class TimelineUniversityAnnouncementsView  : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewTimelineUniversityAnnouncementBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(title: String) {
        binding.ivAnnounce.show(R.drawable.ic_megaphone)
        binding.tvTitle.text = title
    }
}