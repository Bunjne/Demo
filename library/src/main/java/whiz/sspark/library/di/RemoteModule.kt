package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.datasource.remote.RetrofitBuilder
import whiz.sspark.library.data.datasource.remote.service.LearningOutcomeService

val remoteModule = module {
    factory<LearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
}