package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.data_source.remote.RetrofitBuilder
import whiz.sspark.library.data.data_source.remote.service.*

val remoteModule = module {
    factory<AbilityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ActivityRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AddCourseService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeListService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeMenuService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<CalendarService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ClassGroupService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ClassMemberService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ClassPostCommentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ExpectOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<HappeningsService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<InstructorClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<InstructorMenuService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<LearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<LearningPathwayService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<NotificationInboxService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<SchoolRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentAdvisoryAppointmentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassAttendanceService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassScheduleService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentExamScheduleService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentMenuService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<TimelineService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
}