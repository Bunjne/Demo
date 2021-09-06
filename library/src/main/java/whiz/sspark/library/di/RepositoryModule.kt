package whiz.sspark.library.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import whiz.sspark.library.data.repository.LearningOutcomeRepositoryImpl

val repositoryModule = module {
    factory { LearningOutcomeRepositoryImpl(androidContext(), get()) }
}