package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.*

val viewModelModule = module {
    viewModel { AbilityViewModel(get()) }
    viewModel { ActivityRecordViewModel(get()) }
    viewModel { AddCourseViewModel(get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { ClassGroupViewModel(get()) }
    viewModel { ClassMemberViewModel(get()) }
    viewModel { ClassPostCommentViewModel(get()) }
    viewModel { ExpectOutcomeViewModel(get()) }
    viewModel { HappeningsViewModel(get()) }
    viewModel { InstructorClassActivityViewModel(get()) }
    viewModel { LearningOutcomeViewModel(get()) }
    viewModel { LearningPathwayViewModel(get()) }
    viewModel { MenuStudentViewModel(get()) }
    viewModel { SchoolRecordViewModel(get()) }
    viewModel { StudentClassActivityViewModel(get()) }
    viewModel { StudentClassAttendanceViewModel(get()) }
    viewModel { StudentClassScheduleViewModel(get()) }
    viewModel { TimelineViewModel(get()) }
}