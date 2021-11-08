package whiz.sspark.library.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import whiz.sspark.library.data.repository.*

val repositoryModule = module {
    factory { AbilityRepositoryImpl(androidContext(), get()) }
    factory { ActivityRecordRepositoryImpl(androidContext(), get()) }
    factory { AddCourseRepositoryImpl(androidContext(), get()) }
    factory { AdviseeAbilityRepositoryImpl(androidContext(), get()) }
    factory { AdviseeActivityRecordRepositoryImpl(androidContext(), get()) }
    factory { AdviseeExpectOutcomeRepositoryImpl(androidContext(), get()) }
    factory { AdviseeLearningOutcomeRepositoryImpl(androidContext(), get()) }
    factory { AdviseeLearningPathwayRepositoryImpl(androidContext(), get()) }
    factory { AdviseeListRepositoryImpl(androidContext(), get()) }
    factory { AdviseeMenuRepositoryImpl(androidContext(), get()) }
    factory { AdviseeSchoolRecordRepositoryImpl(androidContext(), get()) }
    factory { AssignmentRepositoryImpl(androidContext(), get()) }
    factory { CalendarRepositoryImpl(androidContext(), get()) }
    factory { ClassAssignmentRepositoryImpl(androidContext(), get()) }
    factory { ClassGroupRepositoryImpl(androidContext(), get()) }
    factory { ClassMemberRepositoryImpl(androidContext(), get(), get()) }
    factory { ClassPostCommentRepositoryImpl(androidContext(), get(), get()) }
    factory { ContactRepositoryImpl(androidContext(), get()) }
    factory { CourseSyllabusRepositoryImpl(androidContext(), get()) }
    factory { EventListRepositoryImpl(androidContext(), get()) }
    factory { ExpectOutcomeRepositoryImpl(androidContext(), get()) }
    factory { HappeningsRepositoryImpl(androidContext(), get(), get()) }
    factory { InstructorAllClassRepositoryImpl(androidContext(), get()) }
    factory { InstructorClassActivityRepositoryImpl(androidContext(), get()) }
    factory { InstructorClassAssignmentDetailRepositoryImpl(androidContext(), get()) }
    factory { InstructorClassScheduleRepositoryImpl(androidContext(), get()) }
    factory { InstructorMenuRepositoryImpl(androidContext(), get()) }
    factory { LearningOutcomeRepositoryImpl(androidContext(), get()) }
    factory { LearningPathwayRepositoryImpl(androidContext(), get()) }
    factory { LikeBySeenByRepositoryImpl(androidContext(), get()) }
    factory { ManageAssignmentRepositoryImpl(androidContext(), get()) }
    factory { NotificationInboxRepositoryImpl(androidContext(), get()) }
    factory { ProfileRepositoryImpl(androidContext(), get()) }
    factory { SchoolRecordRepositoryImpl(androidContext(), get()) }
    factory { StudentAllClassRepositoryImpl(androidContext(), get()) }
    factory { StudentClassActivityRepositoryImpl(androidContext(), get()) }
    factory { StudentClassAttendanceRepositoryImpl(androidContext(), get()) }
    factory { StudentClassScheduleRepositoryImpl(androidContext(), get()) }
    factory { StudentExamScheduleRepositoryImpl(androidContext(), get()) }
    factory { StudentMenuRepositoryImpl(androidContext(), get()) }
    factory { TimelineRepositoryImpl(androidContext(), get(), get()) }
}