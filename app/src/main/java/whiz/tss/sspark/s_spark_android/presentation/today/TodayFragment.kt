package whiz.tss.sspark.s_spark_android.presentation.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import whiz.tss.sspark.s_spark_android.databinding.FragmentTodayBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class TodayFragment : BaseFragment() {

    companion object {
        fun newInstance() = TodayFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.stvPage.init(titles = arrayOf(resources.getString(R.string.today_timeline), resources.getString(R.string.today_happenings)),
            onTabClicked ={
                when (it) {
                    0 -> if (!isFragmentVisible(timelineFragmentId)) renderFragment(TimelineFragment.newInstance(), timelineFragmentId)
                    1 -> if (!isFragmentVisible(highlightFragmentId)) renderFragment(HighlightFragment.newInstance(), highlightFragmentId)
                }

                currentFragment = it
            }
        )

        stvPage.selectTab(0)
    }

    override fun observeView() {

    }

    override fun observeData() {

    }

    override fun observeError() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}