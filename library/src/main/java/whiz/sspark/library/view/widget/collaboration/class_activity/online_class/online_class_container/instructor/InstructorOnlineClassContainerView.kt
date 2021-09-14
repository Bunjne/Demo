package whiz.sspark.library.view.widget.collaboration.class_activity.online_class.online_class_container.instructor

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.databinding.ViewInstructorOnlineClassContainerBinding
import whiz.sspark.library.view.widget.collaboration.class_activity.online_class.OnlineClassAdapter

class InstructorOnlineClassContainerView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewInstructorOnlineClassContainerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(platformOnlineClasses: List<PlatformOnlineClass>,
             onShowAllOnlineClassPlatformsClicked: () -> Unit,
             onOnlineClassPlatformClicked: (String) -> Unit) {
        with(binding.rvPlatformOnline) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = OnlineClassAdapter(
                context = context,
                items = platformOnlineClasses,
                onOnlineClassClicked = onOnlineClassPlatformClicked
            )
        }

        binding.ivOption.setOnClickListener {
            onShowAllOnlineClassPlatformsClicked()
        }

        if (platformOnlineClasses.isNotEmpty()) {
            binding.tvTitle.visibility = View.GONE
            binding.rvPlatformOnline.visibility = View.VISIBLE
        } else {
            binding.tvTitle.visibility = View.VISIBLE
            binding.rvPlatformOnline.visibility = View.GONE
        }
    }
}