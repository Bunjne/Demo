package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.*

val viewModelModule = module {
    viewModel { ClassGroupViewModel(get()) }
    viewModel { ClassMemberViewModel(get()) }
    viewModel { ClassPostCommentViewModel(get()) }
    viewModel { ExpectOutcomeViewModel(get()) }
    viewModel { HappeningsViewModel(get()) }
    viewModel { InstructorClassActivityViewModel(get()) }
    viewModel { LearningOutcomeViewModel(get()) }
    viewModel { MenuStudentViewModel(get()) }
    viewModel { NotificationInboxViewModel(get()) }
    viewModel { SchoolRecordViewModel(get()) }
    viewModel { StudentClassActivityViewModel(get()) }
    viewModel { StudentClassAttendanceViewModel(get()) }
    viewModel { TimelineViewModel(get()) }
}