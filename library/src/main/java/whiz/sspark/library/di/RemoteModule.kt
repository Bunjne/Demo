package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.dataSource.remote.RetrofitBuilder
import whiz.sspark.library.data.dataSource.remote.service.TimelineService
import whiz.sspark.library.data.dataSource.remote.service.v3.HappeningsServiceV3
import whiz.sspark.library.data.dataSource.remote.service.v3.TimelineServiceV3

val remoteModule = module {
    factory<HappeningsServiceV3> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<TimelineService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<TimelineServiceV3> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
}