package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.LearningOutcomeViewModel

val viewModelModule = module {
    viewModel { LearningOutcomeViewModel(get()) }
}