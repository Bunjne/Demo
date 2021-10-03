package whiz.sspark.library.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import whiz.sspark.library.data.repository.*

val repositoryModule = module {
    factory { AbilityRepositoryImpl(androidContext(), get()) }
    factory { ActivityRecordRepositoryImpl(androidContext(), get()) }
    factory { AddCourseRepositoryImpl(androidContext(), get()) }
    factory { (userId: String) -> AssignmentRepositoryImpl(get { parametersOf(userId)}) }
    factory { ClassGroupRepositoryImpl(androidContext(), get()) }
    factory { ClassMemberRepositoryImpl(androidContext(), get(), get()) }
    factory { ClassPostCommentRepositoryImpl(androidContext(), get(), get()) }
    factory { ExpectOutcomeRepositoryImpl(androidContext(), get()) }
    factory { HappeningsRepositoryImpl(androidContext(), get(), get()) }
    factory { InstructorClassActivityRepositoryImpl(androidContext(), get()) }
    factory { LearningOutcomeRepositoryImpl(androidContext(), get()) }
    factory { LearningPathwayRepositoryImpl(androidContext(), get()) }
    factory { MenuStudentRepositoryImpl(androidContext(), get()) }
    factory { SchoolRecordRepositoryImpl(androidContext(), get()) }
    factory { StudentClassActivityRepositoryImpl(androidContext(), get()) }
    factory { StudentClassAttendanceRepositoryImpl(androidContext(), get()) }
    factory { StudentClassScheduleRepositoryImpl(androidContext(), get()) }
    factory { TimelineRepositoryImpl(androidContext(), get(), get()) }
}