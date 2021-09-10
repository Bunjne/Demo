package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.ContactViewModel
import whiz.sspark.library.data.viewModel.HappeningsViewModel
import whiz.sspark.library.data.viewModel.LearningOutcomeViewModel
import whiz.sspark.library.data.viewModel.TimelineViewModel

val viewModelModule = module {
    viewModel { HappeningsViewModel(get()) }
    viewModel { LearningOutcomeViewModel(get()) }
    viewModel { TimelineViewModel(get()) }
    viewModel { ContactViewModel(get()) }
}