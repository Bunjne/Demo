package whiz.tss.sspark.s_spark_android.presentation.school_record

import android.os.Bundle
import whiz.sspark.library.data.entity.DataWrapperX
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    override fun initView() {
        binding.vSchoolRecord.init(
            title = "ผลการเรียน ม.3",
            selectedTerm = "1/2564",
            segmentTitles = listOf("AAA", "BBB", "CCC"),
            onSelectTerm = {

            },
            onSegmentClicked = {
                currentSegment = it
                when(it) {
                    LEARNING_OUTCOME_VIEW -> binding.vSchoolRecord.renderFragment(supportFragmentManager, JuniorLearningOutcomeFragment.newInstance(), LEARNING_OUTCOME_VIEW)
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
        binding.vSchoolRecord.setLatestUpdatedText(null)
    }
}