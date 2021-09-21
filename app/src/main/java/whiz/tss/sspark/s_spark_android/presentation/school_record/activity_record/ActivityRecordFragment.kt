package whiz.tss.sspark.s_spark_android.presentation.school_record.activity_record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.ActivityDTO
import whiz.sspark.library.data.entity.ActivityRecord
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.viewModel.ActivityRecordViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toNullableJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.school_record.activity_record.ActivityRecordAdapter
import whiz.tss.sspark.s_spark_android.databinding.FragmentActivityRecordBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

class ActivityRecordFragment: BaseFragment() {

    companion object {
        fun newInstance(termId: String) = ActivityRecordFragment().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
            }
        }
    }

    private val viewModel: ActivityRecordViewModel by viewModel()

    private val termId by lazy {
        arguments?.getString("termId") ?: ""
    }

    private var _binding: FragmentActivityRecordBinding? = null
    private val binding get() = _binding!!
    private var listener: OnRefresh? = null

    private var dataWrapper: DataWrapperX<Any>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentActivityRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = if (parentFragment != null) {
            parentFragment as OnRefresh
        } else {
            activity as OnRefresh
        }

        initView()

        if (savedInstanceState != null) {
            dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject()

            if (dataWrapper != null) {
                val activities = dataWrapper?.data?.toJson()?.toObjects(Array<ActivityDTO>::class.java) ?: listOf()
                updateAdapterItem(activities)

                listener?.onSetLatestUpdatedText(dataWrapper)
            } else {
                viewModel.getActivityRecord(termId)
            }
        } else {
            viewModel.getActivityRecord(termId)
        }
    }

    override fun initView() {
        binding.vActivityRecord.init {
            viewModel.getActivityRecord(termId)
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vActivityRecord.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            listener?.onSetLatestUpdatedText(dataWrapper)
        }
    }

    override fun observeData() {
        viewModel.activityRecordResponse.observe(this) {
            it?.let {
                updateAdapterItem(it)
            }
        }
    }

    override fun observeError() {
        viewModel.activityRecordErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }
    }

    private fun updateAdapterItem(activities: List<ActivityDTO>) {
        val items: MutableList<ActivityRecordAdapter.Item> = mutableListOf()

        activities.forEach {
            items.add(ActivityRecordAdapter.Item(title = it.name))

            it.activities.forEach {
                val activity = ActivityRecord(
                    title = it.name,
                    isCompleted = it.isCompleted,
                    description = it.comment
                )

                items.add(ActivityRecordAdapter.Item(activity = activity))
            }
        }

        binding.vActivityRecord.updateItem(items)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            dataWrapper?.let {
                listener?.onSetLatestUpdatedText(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapper", dataWrapper?.toNullableJson())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnRefresh {
        fun onSetLatestUpdatedText(data: DataWrapperX<Any>?)
    }
}