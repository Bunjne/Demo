package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.LearningOutcomeViewModel
import whiz.sspark.library.data.viewModel.MenuViewModel

val viewModelModule = module {
    viewModel { LearningOutcomeViewModel(get()) }
    viewModel { MenuViewModel(get()) }
}