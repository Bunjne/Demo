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
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.onlineClassLabelColor))
                binding.ivOnlineArrow.setColorFilter(ContextCompat.getColor(context, R.color.onlineClassLabelColor))
            }

            Platform.GoogleClassroom.type -> {
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.external_online_class_normal))
                binding.ivOnlineArrow.setColorFilter(ContextCompat.getColor(context, R.color.external_online_class_normal))
            }

            Platform.Moodle.type -> {
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.external_online_class_moodle))
                binding.ivOnlineArrow.setColorFilter(ContextCompat.getColor(context, R.color.external_online_class_moodle))
            }

            Platform.Other.type -> {
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.external_online_class_normal))
                binding.ivOnlineArrow.setColorFilter(ContextCompat.getColor(context, R.color.external_online_class_normal))
            }
        }

        setOnClickListener {
            val url = platform.url ?: ""
            onOnlineClassClicked(url)
        }
    }
}