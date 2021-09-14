package whiz.tss.sspark.s_spark_android.widget.collaboration

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ViewCollaborationProfileHeaderBinding
import whiz.tss.sspark.s_spark_android.utility.ProfileManager
import kotlin.coroutines.CoroutineContext

class CollaborationProfileHeader : ConstraintLayout, CoroutineScope {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val binding by lazy {
        ViewCollaborationProfileHeaderBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val profileManager by lazy {
        ProfileManager(context)
    }

    fun init(backgroundDrawable: GradientDrawable,
             onBackPressed: () -> Unit = {
                 (context as Activity).onBackPressed()
             }) {
        launch {
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Job().cancel()
    }
}