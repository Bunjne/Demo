package whiz.sspark.library.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import whiz.sspark.library.data.repository.HappeningsRepositoryImpl
import whiz.sspark.library.data.repository.LearningOutcomeRepositoryImpl
import whiz.sspark.library.data.repository.MenuStudentRepositoryImpl
import whiz.sspark.library.data.repository.TimelineRepositoryImpl

val repositoryModule = module {
    factory { HappeningsRepositoryImpl(androidContext(), get(), get()) }
    factory { LearningOutcomeRepositoryImpl(androidContext(), get()) }
    factory { MenuStudentRepositoryImpl(androidContext(), get()) }
    factory { TimelineRepositoryImpl(androidContext(), get(), get()) }
}