package whiz.sspark.library.view.widget.collaboration.class_activity.online_class

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.databinding.ViewOnlineClassContainerBinding
import whiz.sspark.library.extension.toDP

class OnlineClassContainerView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var platformList = mutableListOf<PlatformOnlineClass>()

    private val binding by lazy {
        ViewOnlineClassContainerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(onShowAllOnlineClassPlatformsClicked: () -> Unit,
             onOnlineClassPlatformClicked: (String) -> Unit) {

        setVisibilityDependOnRole()

        with(binding.rvPlatformOnline) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = OnlineClassAdapter(
                context = context,
                items = platformList,
                onOnlineClassClicked = onOnlineClassPlatformClicked
            )
        }

        binding.ivOption.setOnClickListener {
            onShowAllOnlineClassPlatformsClicked()
        }
    }

    fun renderOnlineClasses(platformOnlineClasses: List<PlatformOnlineClass>) {
        with(platformList) {
            clear()
            addAll(platformOnlineClasses)
        }

        binding.rvPlatformOnline.adapter?.notifyDataSetChanged()

        when {
            platformList.isNotEmpty() -> {
                binding.tvTitle.visibility = View.GONE
                binding.rvPlatformOnline.visibility = View.VISIBLE
            }
            else -> {
                if (USparkLibrary.isStudent) {
                    binding.tvTitle.visibility = View.GONE
                } else {
                    binding.tvTitle.visibility = View.VISIBLE
                }
                binding.rvPlatformOnline.visibility = View.GONE
            }
        }
    }

    private fun setVisibilityDependOnRole() {
        if (USparkLibrary.isStudent) {
            binding.tvTitle.visibility = View.GONE
            binding.ivOption.visibility = View.GONE
            val layoutParam = binding.clRoot.layoutParams as? MarginLayoutParams
            layoutParam?.setMargins(0, 0, 0, 0)
            binding.clRoot.setPadding(0, 0, 0, 0)
            binding.clRoot.requestLayout()
            binding.clRoot.background = ContextCompat.getDrawable(context, R.color.transparentColor)
            binding.rvPlatformOnline.setPadding(16.toDP(context), 0, 16.toDP(context), 0)
        } else {
            binding.tvTitle.visibility = View.VISIBLE
            binding.ivOption.visibility = View.VISIBLE
            binding.clRoot.background = ContextCompat.getDrawable(context, R.drawable.bg_online_class_container)
        }
    }
}