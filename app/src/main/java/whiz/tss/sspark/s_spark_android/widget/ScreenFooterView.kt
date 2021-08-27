package whiz.tss.sspark.s_spark_android.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.tss.sspark.s_spark_android.BuildConfig
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ViewScreenFooterBinding

class ScreenFooterView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewScreenFooterBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        init()
    }

    private fun init() {
        binding.appName.text = resources.getString(R.string.app_version, resources.getString(R.string.app_name), BuildConfig.VERSION_CODE.toString())
        binding.tvSchoolName.text = resources.getString(R.string.school_name)
    }
}