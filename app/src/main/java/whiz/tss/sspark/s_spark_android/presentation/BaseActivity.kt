package whiz.tss.sspark.s_spark_android.presentation

import android.os.Bundle
import com.akexorcist.localizationactivity.ui.LocalizationActivity

abstract class BaseActivity: LocalizationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        observeView()
        observeData()
        observeError()
    }

    abstract fun initView()
    abstract fun observeView()
    abstract fun observeData()
    abstract fun observeError()
}