package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.datasource.remote.service.LearningOutcomeService
import whiz.sspark.library.data.dataSource.remote.RetrofitBuilder
import whiz.sspark.library.data.dataSource.remote.service.TimelineService
import whiz.sspark.library.data.dataSource.remote.service.HappeningsService

val remoteModule = module {
    factory<HappeningsService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<LearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<TimelineService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
}