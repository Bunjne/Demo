package whiz.tss.sspark.s_spark_android.presentation.class_schedule.all_class

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.ClassScheduleAllClassDTO
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.data.viewModel.InstructorAllClassViewModel
import whiz.sspark.library.extension.toJson
import whiz.tss.sspark.s_spark_android.R

class InstructorClassScheduleAllClassBottomSheetDialog: StudentClassScheduleAllClassBottomSheetDialog(){

    companion object {
        fun newInstance(term: Term) = InstructorClassScheduleAllClassBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
            }
        }
    }

    override val viewModel: InstructorAllClassViewModel by viewModel()

    override fun getCourseTitle(classScheduleAllClassDTO: ClassScheduleAllClassDTO) = resources.getString(R.string.class_schedule_instructor_course_title, classScheduleAllClassDTO.code, classScheduleAllClassDTO.sectionNumber, classScheduleAllClassDTO.name)
}