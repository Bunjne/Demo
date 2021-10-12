package whiz.tss.sspark.s_spark_android.presentation.school_record

import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.data.entity.convertToJuniorAdvisee
import whiz.sspark.library.data.entity.convertToSeniorAdvisee
import whiz.sspark.library.data.viewModel.AdviseeSchoolRecordViewModel
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.isPrimaryHighSchool
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.presentation.school_record.ability.AbilityFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.activity_record.ActivityRecordFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.AdviseeJuniorLearningOutcomeFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.AdviseeSeniorLearningOutcomeFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.JuniorLearningOutcomeFragment
import whiz.tss.sspark.s_spark_android.presentation.school_record.learning_outcome.SeniorLearningOutcomeFragment

open class AdviseeSchoolRecordActivity : SchoolRecordActivity(){

    override val viewModel: AdviseeSchoolRecordViewModel by viewModel()

    private val student by lazy {
        intent?.getStringExtra("student")?.toObject<Student>()!!
    }

    override fun initView() {
        super.initView()

        val advisee = if (SSparkApp.role == RoleType.INSTRUCTOR_JUNIOR) {
            student.convertToJuniorAdvisee()
        } else {
            student.convertToSeniorAdvisee()
        }

        binding.vSchoolRecord.showAdviseeProfile(advisee)
    }

    override fun getTerms() {
        viewModel.getTerms(studentId = student.id)
    }

    override fun renderFragment(fragmentId: Int) {
        currentSegment = fragmentId
        when(currentSegment) {
            LEARNING_OUTCOME_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, AdviseeJuniorLearningOutcomeFragment.newInstance(currentTerm.id, student), LEARNING_OUTCOME_FRAGMENT)
                } else {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, AdviseeSeniorLearningOutcomeFragment.newInstance(currentTerm.id, student), LEARNING_OUTCOME_FRAGMENT)
                }
            }
            ACTIVITY_AND_ABILITY_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, ActivityRecordFragment.newInstance(currentTerm.id, student.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                } else {
                    binding.vSchoolRecord.renderFragment(supportFragmentManager, AbilityFragment.newInstance(currentTerm.id, student.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                }
            }
            //TODO wait implement other screen
        }
    }

    override fun forceRenderNewFragment(fragmentId: Int) {
        currentSegment = fragmentId
        when(currentSegment) {
            LEARNING_OUTCOME_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, AdviseeJuniorLearningOutcomeFragment.newInstance(currentTerm.id, student), LEARNING_OUTCOME_FRAGMENT)
                } else {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, AdviseeSeniorLearningOutcomeFragment.newInstance(currentTerm.id, student), LEARNING_OUTCOME_FRAGMENT)
                }
            }
            ACTIVITY_AND_ABILITY_FRAGMENT -> {
                if (isPrimaryHighSchool(currentTerm.academicGrade!!)) {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, ActivityRecordFragment.newInstance(currentTerm.id, student.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                } else {
                    binding.vSchoolRecord.forceRenderNewFragment(supportFragmentManager, AbilityFragment.newInstance(currentTerm.id, student.id), ACTIVITY_AND_ABILITY_FRAGMENT)
                }
            }
            //TODO wait implement other screen
        }
    }
}