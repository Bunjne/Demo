package whiz.tss.sspark.s_spark_android.presentation.school_record

import android.os.Bundle
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.databinding.ActivitySchoolRecordBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.JuniorLearningOutcomeFragment

class SchoolRecordActivity : BaseActivity(), JuniorLearningOutcomeFragment.OnRefresh {

    companion object {
        val LEARNING_OUTCOME_VIEW = 0
    }

    private val binding by lazy {
        ActivitySchoolRecordBinding.inflate(layoutInflater)
    }

    private var currentSegment = 0
    private var currentSemesterId = 1 //TODO wait confirm object from API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    override fun initView() {
        val titles = if (SSparkApp.role == RoleType.JUNIOR) {
            resources.getStringArray(R.array.junior_school_record_segment).toList()
        } else {
            resources.getStringArray(R.array.senior_school_record_segment).toList()
        }

        binding.vSchoolRecord.init(
            title = "ผลการเรียน ม.3", //TODO wait confirm object from API
            selectedTerm = "1/2564", //TODO wait confirm object from API
            segmentTitles = titles,
            onSelectTerm = {
                           //TODO wait confirm UI
            },
            onSegmentClicked = {
                currentSegment = it
                when(it) {
                    LEARNING_OUTCOME_VIEW -> binding.vSchoolRecord.renderFragment(supportFragmentManager, JuniorLearningOutcomeFragment.newInstance(currentSemesterId), LEARNING_OUTCOME_VIEW)
                    //TODO wait implement other screen
                }
            }
        )

        binding.vSchoolRecord.setLatestUpdatedText(null)
    }

    override fun observeView() {

    }

    override fun observeData() {

    }

    override fun observeError() {

    }

    override fun onRefresh(data: DataWrapperX<Any>?) {
        binding.vSchoolRecord.setLatestUpdatedText(data)
    }
}