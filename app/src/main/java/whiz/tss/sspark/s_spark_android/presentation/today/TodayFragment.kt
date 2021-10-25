package whiz.tss.sspark.s_spark_android.presentation.today

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.enum.TodayFragmentId
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.toColor
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentTodayBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.today.happening.HappeningsFragment
import whiz.tss.sspark.s_spark_android.presentation.today.timeline.TimelineFragment

class TodayFragment : BaseFragment(), TimelineFragment.OnUpdateAqi {

    companion object {
        fun newInstance() = TodayFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private var currentFragment = -1
    private var isBackgroundUpdated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun initView() {
        binding.stvPage.init(
            titles = arrayOf(resources.getString(R.string.today_timeline), resources.getString(R.string.today_happenings)),
            onTabClicked = {
                when (it) {
                    0 -> if (!isFragmentVisible(TodayFragmentId.TIMELINE.id)) renderFragment(TimelineFragment.newInstance(), TodayFragmentId.TIMELINE.id)
                    1 -> if (!isFragmentVisible(TodayFragmentId.HAPPENINGS.id)) renderFragment(HappeningsFragment.newInstance(), TodayFragmentId.HAPPENINGS.id)
                }

                currentFragment = it
            }
        )

        binding.stvPage.selectTab(0)
    }

    override fun observeView() {

    }

    override fun observeData() {

    }

    override fun observeError() {

    }

    private fun isFragmentVisible(id: Int) = currentFragment == id

    private fun renderFragment(fragment: Fragment, fragmentId: Int) {
        val fragmentList = childFragmentManager.fragments
        val targetFragment = childFragmentManager.findFragmentByTag(fragmentId.toString())

        fragmentList.forEach {
            childFragmentManager.beginTransaction()
                .hide(it)
                .commit()
        }

        targetFragment?.let {
            childFragmentManager.beginTransaction()
                .show(it)
                .commit()
        } ?: childFragmentManager.beginTransaction()
            .add(R.id.flContainer, fragment, fragmentId.toString())
            .commit()
    }

    override fun onUpdateAqi(aqiIconUrl: String, weatherIconUrl: String, backgroundImageUrl: String, aqi: Int, color: String) {
        if (weatherIconUrl.isBlank() || aqi == 0) {
            binding.cvWeather.visibility = View.INVISIBLE
        } else {
            binding.cvWeather.visibility = View.VISIBLE
            binding.ivAqi.show(aqiIconUrl)
            binding.ivWeatherIcon.show(weatherIconUrl)

            binding.tvApi.text = "$aqi"
            if (color.isNotBlank()) {
                binding.cvAqi.setCardBackgroundColor(color.toColor(ContextCompat.getColor(requireContext(), R.color.viewBaseFourthColor)))
            }
        }

        if (!isBackgroundUpdated) {
            binding.ivBackground.show(backgroundImageUrl)
            isBackgroundUpdated = true
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (SSparkLibrary.isAutoDarkModeEnabled) {
            viewModelStore.clear()
        }

        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}