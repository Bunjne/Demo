package whiz.tss.sspark.s_spark_android.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.Profile
import whiz.sspark.library.view.general.loading_dialog.SSparkLoadingDialog
import whiz.tss.sspark.s_spark_android.utility.ProfileManager

abstract class BaseFragment : Fragment() {

    protected val profileManager by lazy {
        ProfileManager(requireContext())
    }

    protected val loadingDialog by lazy {
        SSparkLoadingDialog(requireContext())
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

    open fun initView() { }
    open fun observeView() { }
    open fun observeData() { }
    open fun observeError() { }
}