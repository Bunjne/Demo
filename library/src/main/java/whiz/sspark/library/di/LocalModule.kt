package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.data.dataSource.local.impl.HappeningsCacheImpl
import whiz.sspark.library.data.dataSource.local.impl.TimelineCacheImpl

val localModule = module {
    factory { HappeningsCacheImpl() }
    factory { TimelineCacheImpl() }
}