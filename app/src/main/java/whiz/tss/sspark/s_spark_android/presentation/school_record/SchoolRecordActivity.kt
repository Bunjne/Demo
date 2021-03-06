package whiz.tss.sspark.s_spark_android.presentation.school_record

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.viewModel.SchoolRecordViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.convertToLocalizeYear
import whiz.sspark.library.utility.getHighSchoolLevel
import whiz.sspark.library.utility.isPrimaryHighSchool
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivitySchoolRecordBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.school_record.ability.AbilityFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.activity_record.ActivityRecordFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.JuniorLearningOutcomeFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.SeniorLearningOutcomeFragment

open class SchoolRecordActivity : BaseActivity(),
    JuniorLearningOutcomeFragment.OnRefresh,
    SeniorLearningOutcomeFragment.OnRefresh,
    ActivityRecordFragment.OnRefresh,
    AbilityFragment.OnRefresh {

    companion object {
        const val LEARNING_OUTCOME_FRAGMENT = 0
        const val ACTIVITY_AND_ABILITY_FRAGMENT = 1
    }

    protected open val viewModel: SchoolRecordViewModel by viewModel()

    protected lateinit var binding: ActivitySchoolRecordBinding
    private var popupMenu: PopupMenu? = null

    protected var currentSegment = -1
    private var savedFragment = -1

    protected lateinit var currentTerm: Term
    private var terms: MutableList<Term> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchoolRecordBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
            initView()

            if (savedFragment != -1) {
                binding.vSchoolRecord.setSelectedTab(savedFragment)
            }

            val isTermSelectable = terms.size > 1
            binding.vSchoolRecord.initMultipleTerm(isTermSelectable) {
                initPopupMenu(it)
            }
        } else {
            getInitialTerm()
            initView()
            getTerms()
        }
    }

    protected open fun getInitialTerm() {
        lifecycleScope.launch {
            profileManager.term.collect {
                it?.let {
                    currentTerm = it
                }
            }
        }
    }

    protected open fun getTerms() {
        viewModel.getTerms()
    }

    override fun initView() {
        val title = resources.getString(R.string.school_record_title, getHighSchoolLevel(currentTerm.academicGrade).toString())
        val termTitle = resources.getString(R.string.school_record_term, currentTerm.term.toString(), convertToLocalizeYear(currentTerm.year))
        val segmentTitles = if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
            resources.getStringArray(R.array.junior_school_record_segment).toList()
        } else {
            resources.getStringArray(R.array.senior_school_record_segment).toList()
        }

        binding.vProfile.init(lifecycle)
        with(binding.vSchoolRecord) {
            init(
                title = title,
                term = termTitle,
                segmentTitles = segmentTitles,
                onSelectTerm = {
                    popupMenu?.show()
                },
                onSegmentClicked = {
                    renderFragment(fragmentId = it)
                }
            )
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    override fun observeData() {
        viewModel.termsResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                with(terms) {
                    clear()
                    addAll(it)
                }

                val isTermSelectable = terms.size > 1
                binding.vSchoolRecord.initMultipleTerm(isTermSelectable) {
                    initPopupMenu(it)
                }
            }
        }
    }

    override fun observeError() {
        viewModel.termsErrorResponse.observe(this) {
            it?.getContentIfNotHandled()?.let {
                showApiResponseXAlert(this, it) {
                    finish()
                }
            }
        }
    }

    private fun updateTerm() {
        val title = resources.getString(R.string.school_record_title, getHighSchoolLevel(currentTerm.academicGrade).toString())
        val term = resources.getString(R.string.school_record_term, currentTerm.term.toString(), convertToLocalizeYear(currentTerm.year))
        val segmentTitles = if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
            resources.getStringArray(R.array.junior_school_record_segment).toList()
        } else {
            resources.getStringArray(R.array.senior_school_record_segment).toList()
        }

        binding.vSchoolRecord.changeTerm(title, term, segmentTitles, currentSegment)
        forceRenderNewFragment(currentSegment)
    }

    protected open fun renderFragment(fragmentId: Int) {
        currentSegment = fragmentId
        when(currentSegment) {
            LEARNING_OUTCOME_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, JuniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                } else {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, SeniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                }
            }
            ACTIVITY_AND_ABILITY_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, ActivityRecordFragment.newInstance(currentTerm.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                } else {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, AbilityFragment.newInstance(currentTerm.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                }
            }
            //TODO wait implement other screen
        }
    }

    protected open fun forceRenderNewFragment(fragmentId: Int) {
        currentSegment = fragmentId
        when(currentSegment) {
            LEARNING_OUTCOME_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, JuniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                } else {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, SeniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                }
            }
            ACTIVITY_AND_ABILITY_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, ActivityRecordFragment.newInstance(currentTerm.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                } else {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, AbilityFragment.newInstance(currentTerm.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                }
            }
            //TODO wait implement other screen
        }
    }

    override fun onSetLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.vSchoolRecord.setLatestUpdatedText(data)
    }

    private fun initPopupMenu(view: View) {
        popupMenu = PopupMenu(this, view).apply {
            setOnMenuItemClickListener {
                val termTitle = it.title
                val splitTerm = termTitle.split("/")

                val term = splitTerm.getOrNull(0)?.toIntOrNull() ?: 0
                val year = splitTerm.getOrNull(1) ?: ""

                val selectedTerm = terms.find { it.term == term && convertToLocalizeYear(it.year) == year }

                if (selectedTerm != null && selectedTerm != currentTerm) {
                    currentTerm = selectedTerm
                    updateTerm()
                }

                true
            }
            menu.clear()

            terms.forEach {
                val selectAbleTerm = resources.getString(
                    R.string.school_record_term,
                    it.term.toString(),
                    convertToLocalizeYear(it.year)
                )
                menu.add(selectAbleTerm)
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedFragment = savedInstanceState.getInt("savedFragment", -1)
        currentTerm = savedInstanceState.getString("currentTerm")?.toObject<Term>()!!
        val restoredTerms = savedInstanceState.getString("terms")?.toObjects(Array<Term>::class.java)!!

        with(terms) {
            clear()
            addAll(restoredTerms)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("savedFragment", currentSegment)
        outState.putString("terms", terms.toJson())
        outState.putString("currentTerm", currentTerm.toJson())
        viewModelStore.clear()
    }
}