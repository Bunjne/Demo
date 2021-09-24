package whiz.sspark.library.view.widget.collaboration.class_activity.online_class

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.data.enum.Platform
import whiz.sspark.library.databinding.ViewOnlineClassItemBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toDP

class OnlineClassItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewOnlineClassItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(platform: PlatformOnlineClass, onOnlineClassClicked: (String) -> Unit) {
        binding.tvTitle.text = if (platform.nameEn != Platform.Other.type) {
            platform.name
        } else {
            platform.displayName
        }
        binding.ivIcon.show(platform.logoUrl!!)

        when (platform.nameEn) {
            Platform.MicrosoftTeams.type -> {
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.microsoftTeamBaseColor))
                binding.ivOnlineArrow.setColorFilter(ContextCompat.getColor(context, R.color.microsoftTeamBaseColor))
            }

            Platform.Moodle.type -> {
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.onlineClassMoodleColor))
                binding.ivOnlineArrow.setColorFilter(ContextCompat.getColor(context, R.color.onlineClassMoodleColor))
            }

            Platform.GoogleClassroom.type,
            Platform.Other.type -> {
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.onlineClassNormalColor))
                binding.ivOnlineArrow.setColorFilter(ContextCompat.getColor(context, R.color.onlineClassNormalColor))
            }
        }

        setOnClickListener {
            val url = platform.url ?: ""
            onOnlineClassClicked(url)
        }
    }
}