package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.data.data_source.local.impl.ClassMemberCacheImpl
import whiz.sspark.library.data.data_source.local.impl.HappeningsCacheImpl
import whiz.sspark.library.data.data_source.local.impl.TimelineCacheImpl
import whiz.sspark.library.data.data_source.local.impl.ContactCacheImpl

val localModule = module {
    factory { ClassMemberCacheImpl() }
    factory { HappeningsCacheImpl() }
    factory { TimelineCacheImpl() }
    factory { ContactCacheImpl() }
}