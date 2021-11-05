package whiz.tss.sspark.s_spark_android.widget.collaboration

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ViewCollaborationProfileHeaderBinding
import whiz.tss.sspark.s_spark_android.utility.ProfileManager

class CollaborationProfileHeader : ConstraintLayout, LifecycleObserver {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var scope: LifecycleCoroutineScope

    private val binding by lazy {
        ViewCollaborationProfileHeaderBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val profileManager by lazy {
        ProfileManager(context)
    }

    fun init(backgroundDrawable: Drawable,
             onBackPressed: () -> Unit = {
                 (context as Activity).onBackPressed()
             }) {
        scope.launch {
            profileManager.profile.collect {
                it?.let {
                    binding.ivProfile.showUserProfileCircle(it.imageUrl ?: "", getGender(it.gender ?: "").type)
                    binding.tvName.text = it.firstName
                    binding.tvCode.text = it.code
                }
            }
        }

        binding.clContainer.background = backgroundDrawable

        with (binding.ivBack) {
            show(R.drawable.ic_navigation_back)

            setOnClickListener {
                onBackPressed()
            }
        }
    }

    fun registerLifecycleOwner(lifecycle: Lifecycle){
        lifecycle.addObserver(this)
        scope = lifecycle.coroutineScope
    }
}