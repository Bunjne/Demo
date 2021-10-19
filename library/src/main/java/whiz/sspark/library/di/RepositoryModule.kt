package whiz.sspark.library.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import whiz.sspark.library.data.repository.*

val repositoryModule = module {
    factory { AbilityRepositoryImpl(androidContext(), get()) }
    factory { ActivityRecordRepositoryImpl(androidContext(), get()) }
    factory { AddCourseRepositoryImpl(androidContext(), get()) }
    factory { AdviseeListRepositoryImpl(androidContext(), get()) }
    factory { AdviseeMenuRepositoryImpl(androidContext(), get()) }
    factory { AssignmentRepositoryImpl(get()) }
    factory { CalendarRepositoryImpl(androidContext(), get()) }
    factory { ClassGroupRepositoryImpl(androidContext(), get()) }
    factory { ClassMemberRepositoryImpl(androidContext(), get(), get()) }
    factory { ClassPostCommentRepositoryImpl(androidContext(), get(), get()) }
    factory { CourseSyllabusRepositoryImpl(androidContext(), get()) }
    factory { ExpectOutcomeRepositoryImpl(androidContext(), get()) }
    factory { HappeningsRepositoryImpl(androidContext(), get(), get()) }
    factory { InstructorClassActivityRepositoryImpl(androidContext(), get()) }
    factory { InstructorMenuRepositoryImpl(androidContext(), get()) }
    factory { LearningOutcomeRepositoryImpl(androidContext(), get()) }
    factory { LearningPathwayRepositoryImpl(androidContext(), get()) }
    factory { NotificationInboxRepositoryImpl(androidContext(), get()) }
    factory { SchoolRecordRepositoryImpl(androidContext(), get()) }
    factory { StudentClassActivityRepositoryImpl(androidContext(), get()) }
    factory { StudentClassAttendanceRepositoryImpl(androidContext(), get()) }
    factory { StudentClassScheduleRepositoryImpl(androidContext(), get()) }
    factory { StudentExamScheduleRepositoryImpl(androidContext(), get()) }
    factory { StudentMenuRepositoryImpl(androidContext(), get()) }
    factory { TimelineRepositoryImpl(androidContext(), get(), get()) }
}