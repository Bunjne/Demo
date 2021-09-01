package whiz.tss.sspark.s_spark_android.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.Profile
import whiz.tss.sspark.s_spark_android.utility.ProfileManager

abstract class BaseFragment : Fragment() {

    protected val profileManager by lazy {
        ProfileManager(requireContext())
    }

    protected var profile: Profile? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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