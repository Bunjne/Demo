package whiz.sspark.library.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import whiz.sspark.library.data.repository.*

val repositoryModule = module {
    factory { ClassPostCommentRepositoryImpl(androidContext(), get(), get()) }
    factory { HappeningsRepositoryImpl(androidContext(), get(), get()) }
    factory { InstructorClassActivityRepositoryImpl(androidContext(), get()) }
    factory { LearningOutcomeRepositoryImpl(androidContext(), get()) }
    factory { MenuStudentRepositoryImpl(androidContext(), get()) }
    factory { StudentClassActivityRepositoryImpl(androidContext(), get()) }
    factory { TimelineRepositoryImpl(androidContext(), get(), get()) }
    factory { ContactRepositoryImpl(androidContext(), get(), get()) }
}