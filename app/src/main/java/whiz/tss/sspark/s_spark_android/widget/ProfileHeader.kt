package whiz.tss.sspark.s_spark_android.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.utility.convertToFullName
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.ViewProfileHeaderBinding
import whiz.tss.sspark.s_spark_android.utility.ProfileManager
import kotlin.coroutines.CoroutineContext

class ProfileHeader : ConstraintLayout, CoroutineScope {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val binding by lazy {
        ViewProfileHeaderBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val profileManager by lazy {
        ProfileManager(context)
    }

    init {
        init()
    }

    fun init(onBackPressed: () -> Unit = {
        (context as Activity).onBackPressed()
    }) {
        launch {
            profileManager.profile.collect {
                it?.let {
                    when (SSparkApp.role) {
                        RoleType.STUDENT_JUNIOR,
                        RoleType.STUDENT_SENIOR -> {
                            binding.tvName.text = it.firstName
                            binding.tvCode.text = it.code
                        }
                        RoleType.INSTRUCTOR_JUNIOR,
                        RoleType.INSTRUCTOR_SENIOR -> {
                            binding.tvName.text = convertToFullName(it.firstName, null, null, it.position)
                            binding.tvCode.text = it.lastName
                        }
                    }

                    binding.ivProfile.post {
                        binding.ivProfile.showUserProfileCircle(it.imageUrl ?: "", getGender(it.gender).type)
                    }
                }
            }
        }

        binding.ivBack.show(R.drawable.ic_back)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun setBackGradientColor(startColor: Int, endColor: Int) {
        binding.cvBack.background_Gradient_Colors = intArrayOf(startColor, endColor)
        binding.cvBack.invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Job().cancel()
    }
}