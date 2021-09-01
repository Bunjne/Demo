package whiz.tss.sspark.s_spark_android.presentation

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.Profile
import whiz.tss.sspark.s_spark_android.utility.ProfileManager

abstract class BaseActivity: LocalizationActivity() {

    protected val profileManager by lazy {
        ProfileManager(this)
    }

    protected var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            profileManager.profile.collect {
                it?.let {
                    profile = it
                }
            }
        }

        observeView()
        observeData()
        observeError()
    }

    abstract fun initView()
    abstract fun observeView()
    abstract fun observeData()
    abstract fun observeError()
}