package whiz.tss.sspark.s_spark_android.presentation.class_schedule

import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.ClassScheduleDTO
import whiz.sspark.library.data.viewModel.InstructorClassScheduleViewModel
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.presentation.class_schedule.all_class.InstructorClassScheduleAllClassBottomSheetDialog

class InstructorClassScheduleActivity : StudentClassScheduleActivity() {

    override val viewModel: InstructorClassScheduleViewModel by viewModel()

    override val title by lazy {
        resources.getString(R.string.class_schedule_instructor_title)
    }

    override fun getCourseDescription(classScheduleDTO: ClassScheduleDTO) = resources.getString(R.string.general_class_room, classScheduleDTO.room)

    override fun getNoClassTitle() = resources.getString(R.string.class_schedule_instructor_no_class)

    override fun getCourseTitle(classScheduleDTO: ClassScheduleDTO) = resources.getString(R.string.class_schedule_instructor_course_title, classScheduleDTO.code, classScheduleDTO.sectionNumber, classScheduleDTO.name)

    override fun showAllClassDialog() {
        InstructorClassScheduleAllClassBottomSheetDialog.newInstance(
            term = currentTerm
        ).show(supportFragmentManager, ALL_CLASS_DIALOG)
    }
}