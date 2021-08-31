package whiz.sspark.library.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import whiz.sspark.library.data.viewModel.HappeningsViewModel
import whiz.sspark.library.data.viewModel.TimelineViewModel

val viewModelModule = module {
    viewModel { HappeningsViewModel(get()) }
    viewModel { TimelineViewModel(get()) }
}