package whiz.tss.sspark.s_spark_android.presentation.school_record

import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.SchoolRecordViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivitySchoolRecordBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.JuniorLearningOutcomeFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.SeniorLearningOutcomeFragment
import whiz.tss.sspark.s_spark_android.utility.getHighSchoolLevel
import whiz.tss.sspark.s_spark_android.utility.isPrimaryHighSchool

class SchoolRecordActivity : BaseActivity(), JuniorLearningOutcomeFragment.OnRefresh, SeniorLearningOutcomeFragment.OnRefresh {

    companion object {
        val LEARNING_OUTCOME_FRAGMENT = 0
    }

    private val viewModel: SchoolRecordViewModel by viewModel()

    private lateinit var binding: ActivitySchoolRecordBinding

    private var currentSegment = -1
    private var lastedFragment = -1

    private lateinit var currentTerm: Term
    private var terms: MutableList<Term> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchoolRecordBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
            initView()
            val isSelectTermAble = terms.isNotEmpty()
            binding.vSchoolRecord.setSelectTermAble(isSelectTermAble)
        } else {
            lifecycleScope.launch {
                profileManager.term.collect {
                    it?.let {
                        currentTerm = it
                        initView()
                        viewModel.getTerms()
                    }
                }
            }
        }
    }

    override fun initView() {
        val title = resources.getString(R.string.school_record_title, getHighSchoolLevel(currentTerm.academicGrade!!).toString())
        val term = resources.getString(R.string.school_record_term, currentTerm.term.toString(), currentTerm.year.toString())
        val segmentTitles = if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
            resources.getStringArray(R.array.junior_school_record_segment).toList()
        } else {
            resources.getStringArray(R.array.senior_school_record_segment).toList()
        }

        with(binding.vSchoolRecord) {
            init(
                title = title,
                term = term,
                segmentTitles = segmentTitles,
                onSelectTerm = {
                    PopupMenu(this@SchoolRecordActivity, it).run {
                        setOnMenuItemClickListener {
                            val termTitle = it.title
                            val splitTerm = termTitle.split("/")

                            val selectedTerm = splitTerm.getOrNull(0)?.toLongOrNull() ?: 0
                            val selectedYear = splitTerm.getOrNull(1)?.toLongOrNull() ?: 0

                            val index = terms.indexOfFirst { it.term == selectedTerm && it.year == selectedYear }
                            if (index != -1) {
                                currentTerm = terms[index]
                                updateTerm()
                            }

                            true
                        }

                        terms.forEach {
                            val selectAbleTerm = resources.getString(R.string.school_record_term, it.term.toString(), it.year.toString())
                            menu.add(selectAbleTerm)
                        }

                        show()
                    }
                },
                onSegmentClicked = {
                    renderFragment(fragmentId = it)
                }
            )

            if (lastedFragment != -1) {
                setSelectedTab(lastedFragment)
            }
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
            it?.let {
                with(terms) {
                    clear()
                    addAll(it)
                }

                val isSelectTermAble = terms.isNotEmpty()
                binding.vSchoolRecord.setSelectTermAble(isSelectTermAble)
            }
        }
    }

    override fun observeError() {
        viewModel.termsErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it) {
                    finish()
                }
            }
        }
    }

    private fun updateTerm() {
        val title = resources.getString(R.string.school_record_title, getHighSchoolLevel(currentTerm.academicGrade!!).toString())
        val term = resources.getString(R.string.school_record_term, currentTerm.term.toString(), currentTerm.year.toString())
        val segmentTitles = if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
            resources.getStringArray(R.array.junior_school_record_segment).toList()
        } else {
            resources.getStringArray(R.array.senior_school_record_segment).toList()
        }

        binding.vSchoolRecord.changeTerm(title, term, segmentTitles, currentSegment)
        forceRenderNewFragment(currentSegment)
    }

    private fun renderFragment(fragmentId: Int) {
        currentSegment = fragmentId
        when(currentSegment) {
            LEARNING_OUTCOME_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, JuniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                } else {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, SeniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                }
            }
            //TODO wait implement other screen
        }
    }

    private fun forceRenderNewFragment(fragmentId: Int) {
        currentSegment = fragmentId
        when(currentSegment) {
            LEARNING_OUTCOME_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, JuniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                } else {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, SeniorLearningOutcomeFragment.newInstance(currentTerm.id), LEARNING_OUTCOME_FRAGMENT)
                }
            }
            //TODO wait implement other screen
        }
    }

    override fun onSetLatestUpdatedText(data: DataWrapperX<Any>?) {
        binding.vSchoolRecord.setLatestUpdatedText(data)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastedFragment = savedInstanceState.getInt("lastedFragment", -1)
        currentTerm = savedInstanceState.getString("currentTerm")?.toObject<Term>()!!
        val restoredTerms = savedInstanceState.getString("terms")?.toObjects(Array<Term>::class.java)!!

        with(terms) {
            clear()
            addAll(restoredTerms)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("lastedFragment", currentSegment)
        outState.putString("terms", terms.toJson())
        outState.putString("currentTerm", currentTerm.toJson())
        viewModelStore.clear()
    }
}