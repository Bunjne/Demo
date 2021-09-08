package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.dataSource.remote.RetrofitBuilder
import whiz.sspark.library.data.datasource.remote.service.TimelineService
import whiz.sspark.library.data.dataSource.remote.service.HappeningsService
import whiz.sspark.library.data.datasource.remote.service.InstructorClassActivityService
import whiz.sspark.library.data.datasource.remote.service.LearningOutcomeService
import whiz.sspark.library.data.datasource.remote.service.StudentClassActivityService

val remoteModule = module {
    factory<HappeningsService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<InstructorClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<LearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<TimelineService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
}