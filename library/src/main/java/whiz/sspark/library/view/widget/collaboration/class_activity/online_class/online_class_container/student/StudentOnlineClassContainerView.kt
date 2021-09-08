package whiz.sspark.library.view.widget.collaboration.class_activity.online_class.online_class_container.student

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.databinding.ViewStudentOnlineClassContainerBinding
import whiz.sspark.library.utility.updateItem
import whiz.sspark.library.view.widget.collaboration.class_activity.online_class.OnlineClassAdapter

class StudentOnlineClassContainerView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var platformList = mutableListOf<PlatformOnlineClass>()

    private val binding by lazy {
        ViewStudentOnlineClassContainerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onOnlineClassPlatformClicked: (String) -> Unit) {
        with(binding.rvPlatformOnline) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = OnlineClassAdapter(
                context = context,
                items = platformList,
                onOnlineClassClicked = onOnlineClassPlatformClicked
            )
        }
    }

    fun renderOnlineClasses(platformOnlineClasses: List<PlatformOnlineClass>) {
        binding.rvPlatformOnline.adapter?.updateItem(platformList, platformOnlineClasses)

        if (platformList.isNotEmpty()) {
            binding.rvPlatformOnline.visibility = View.VISIBLE
        } else {
            binding.rvPlatformOnline.visibility = View.GONE
        }
    }
}