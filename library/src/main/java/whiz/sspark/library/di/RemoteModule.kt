package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.data_source.remote.service.MenuStudentService
import whiz.sspark.library.data.data_source.remote.RetrofitBuilder
import whiz.sspark.library.data.data_source.remote.service.TimelineService
import whiz.sspark.library.data.data_source.remote.service.HappeningsService
import whiz.sspark.library.data.data_source.remote.service.LearningOutcomeService
import whiz.sspark.library.data.dataSource.remote.service.ContactService

val remoteModule = module {
    factory<HappeningsService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<LearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<MenuStudentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<TimelineService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ContactService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
}