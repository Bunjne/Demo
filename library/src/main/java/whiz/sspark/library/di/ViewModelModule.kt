package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.*

val viewModelModule = module {
    viewModel { AbilityViewModel(get()) }
    viewModel { ActivityRecordViewModel(get()) }
    viewModel { AddCourseViewModel(get()) }
    viewModel { AdviseeAbilityViewModel(get()) }
    viewModel { AdviseeActivityRecordViewModel(get()) }
    viewModel { AdviseeClassScheduleAllClassViewModel(get()) }
    viewModel { AdviseeClassScheduleViewModel(get()) }
    viewModel { AdviseeExpectOutcomeViewModel(get()) }
    viewModel { AdviseeLearningOutcomeViewModel(get()) }
    viewModel { AdviseeLearningPathwayViewModel(get()) }
    viewModel { AdviseeListViewModel(get()) }
    viewModel { AdviseeMenuViewModel(get()) }
    viewModel { AdviseeSchoolRecordViewModel(get()) }
    viewModel { AssignmentViewModel(get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { ClassGroupViewModel(get()) }
    viewModel { ClassMemberViewModel(get()) }
    viewModel { ClassPostCommentViewModel(get()) }
    viewModel { CourseSyllabusViewModel(get()) }
    viewModel { EventListViewModel(get()) }
    viewModel { ExpectOutcomeViewModel(get()) }
    viewModel { HappeningsViewModel(get()) }
    viewModel { InstructorAllClassViewModel(get()) }
    viewModel { InstructorClassActivityViewModel(get()) }
    viewModel { InstructorClassScheduleViewModel(get()) }
    viewModel { InstructorMenuViewModel(get()) }
    viewModel { LearningOutcomeViewModel(get()) }
    viewModel { LearningPathwayViewModel(get()) }
    viewModel { LikeBySeenByViewModel(get()) }
    viewModel { NotificationInboxViewModel(get()) }
    viewModel { SchoolRecordViewModel(get()) }
    viewModel { StudentClassActivityViewModel(get()) }
    viewModel { StudentClassAssignmentViewModel(get()) }
    viewModel { StudentClassAttendanceViewModel(get()) }
    viewModel { StudentClassScheduleAllClassViewModel(get()) }
    viewModel { StudentClassScheduleViewModel(get()) }
    viewModel { StudentExamScheduleViewModel(get()) }
    viewModel { StudentMenuViewModel(get()) }
    viewModel { TimelineViewModel(get()) }
}