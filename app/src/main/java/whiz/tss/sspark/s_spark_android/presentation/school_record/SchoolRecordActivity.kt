package whiz.tss.sspark.s_spark_android.presentation.school_record

import android.os.Bundle
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.ActivitySchoolRecordBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.JuniorLearningOutcomeFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.SeniorLearningOutcomeFragment

class SchoolRecordActivity : BaseActivity(), JuniorLearningOutcomeFragment.OnRefresh, SeniorLearningOutcomeFragment.OnRefresh {

    companion object {
        val LEARNING_OUTCOME_FRAGMENT = 0
    }

    private lateinit var binding: ActivitySchoolRecordBinding

    private var currentSegment = -1
    private var lastedFragment = -1
    private var termId = "0" //TODO wait confirm object from API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchoolRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        initView()
    }

    override fun initView() {
        val titles = if (SSparkApp.role == RoleType.JUNIOR) {
            resources.getStringArray(R.array.junior_school_record_segment).toList()
        } else {
            resources.getStringArray(R.array.senior_school_record_segment).toList()
        }

        binding.vSchoolRecord.init(
            title = "ผลการเรียน ม.3",
            selectedTerm = "1/2564",
            segmentTitles = titles,
            onSelectTerm = {
               //TODO wait confirm UI
            },
            onSegmentClicked = {
                renderFragment(fragmentId = it)
            }
        )

        if (lastedFragment != -1) {
            renderFragment(fragmentId = lastedFragment)
            lastedFragment = -1
        }
    }

    private fun renderFragment(fragmentId: Int) {
        currentSegment = fragmentId
        when(currentSegment) {
            LEARNING_OUTCOME_FRAGMENT -> {
                if (SSparkApp.role == RoleType.JUNIOR) {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, JuniorLearningOutcomeFragment.newInstance(termId), LEARNING_OUTCOME_FRAGMENT)
                } else {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, SeniorLearningOutcomeFragment.newInstance(termId), LEARNING_OUTCOME_FRAGMENT)
                }
            }
            //TODO wait implement other screen
        }
    }

    override fun onRefresh(data: DataWrapperX<Any>?) {
        binding.vSchoolRecord.setLatestUpdatedText(data)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        lastedFragment = savedInstanceState.getInt("lastedFragment", -1)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("lastedFragment", currentSegment)
    }
}