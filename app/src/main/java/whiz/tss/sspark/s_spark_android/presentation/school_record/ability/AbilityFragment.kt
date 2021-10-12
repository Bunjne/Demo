package whiz.tss.sspark.s_spark_android.presentation.school_record.ability

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.AbilityDTO
import whiz.sspark.library.data.entity.Ability
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.viewModel.AbilityViewModel
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toNullableJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.school_record.ability.AbilityAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentAbilityBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment

open class AbilityFragment: BaseFragment() {

    companion object {
        fun newInstance(termId: String) = AbilityFragment().apply {
            arguments = Bundle().apply {
                putString("termId", termId)
            }
        }
    }

    protected open val viewModel: AbilityViewModel by viewModel()

    protected val termId by lazy {
        arguments?.getString("termId") ?: ""
    }

    protected open val indicators: Array<String> by lazy {
        resources.getStringArray(R.array.school_record_senior_indicator)
    }

    private var _binding: FragmentAbilityBinding? = null
    private val binding get() = _binding!!
    private var listener: OnRefresh? = null

    private var dataWrapper: DataWrapperX<Any>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAbilityBinding.inflate(inflater, container, false)
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
        }

        if (dataWrapper != null) {
            val abilities = dataWrapper?.data?.toJson()?.toObjects(Array<AbilityDTO>::class.java) ?: listOf()
            updateAdapterItem(abilities)

            listener?.onSetLatestUpdatedText(dataWrapper)
        } else {
            getAbility()
        }
    }

    protected open fun getAbility() {
        viewModel.getAbility(termId)
    }

    override fun initView() {
        binding.vAbility.init {
            getAbility()
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAbility.setSwipeRefreshLoading(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            listener?.onSetLatestUpdatedText(dataWrapper)
        }
    }

    override fun observeData() {
        viewModel.abilityResponse.observe(this) {
            it?.let {
                updateAdapterItem(it)
            }
        }
    }

    override fun observeError() {
        viewModel.abilityErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }
    }

    private fun updateAdapterItem(abilities: List<AbilityDTO>) {
        val items: MutableList<AbilityAdapter.Item> = mutableListOf()

        abilities.forEach {
            items.add(AbilityAdapter.Item(title = it.name))

            it.abilities.forEach {
                val value = (it.value / it.fullValue) * indicators.size
                val ability = Ability(
                    title = it.name,
                    value = value,
                    indicators = indicators.toList()
                )

                items.add(AbilityAdapter.Item(ability = ability))
            }
        }
        binding.vAbility.updateItem(items)
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