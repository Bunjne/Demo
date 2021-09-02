package whiz.tss.sspark.s_spark_android.presentation.school_record

import android.os.Bundle
import whiz.tss.sspark.s_spark_android.databinding.ActivitySchoolRecordBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity

class SchoolRecordActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySchoolRecordBinding.inflate(layoutInflater)
    }

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
}