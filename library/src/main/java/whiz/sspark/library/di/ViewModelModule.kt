package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.*

val viewModelModule = module {
    viewModel { AddCourseViewModel(get()) }
    viewModel { ClassMemberViewModel(get()) }
    viewModel { ClassPostCommentViewModel(get()) }
    viewModel { HappeningsViewModel(get()) }
    viewModel { InstructorClassActivityViewModel(get()) }
    viewModel { LearningOutcomeViewModel(get()) }
    viewModel { LearningPathwayViewModel(get()) }
    viewModel { MenuStudentViewModel(get()) }
    viewModel { StudentClassActivityViewModel(get()) }
    viewModel { TimelineViewModel(get()) }
}