package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.databinding.FragmentClassGroupBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import java.util.*

class ClassGroupFragment : BaseFragment() {

    companion object {
        fun newInstance() = ClassGroupFragment()
    }

    private var _binding: FragmentClassGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClassGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            initView()

            viewModel.listClassGroupV3(true)
        }
    }

    override fun initView() {
        with (binding.vClassGroup) {

        }
    }

    override fun observeView() {
        viewModel.state.classCollaborationSemesterLoading.observe(this, Observer { isLoading ->
            binding.vClassGroup.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.classCollaborationSemester.observe(this, Observer {
            it?.let { semester ->
                binding.vClassGroup.renderData()
            }
        })
    }

    override fun observeError() {
        viewModel.classCollaborationSemesterErrorResponse.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }
        })
    }
}