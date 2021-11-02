package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.data_source.remote.RetrofitBuilder
import whiz.sspark.library.data.data_source.remote.service.*

val remoteModule = module {
    factory<AbilityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ActivityRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AddCourseService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeAbilityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeActivityRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeExpectOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeLearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeListService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeMenuService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AdviseeSchoolRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AssignmentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<CalendarService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ClassGroupService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ClassMemberService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ClassPostCommentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<CourseSyllabusService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ExpectOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<HappeningsService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<InstructorAllClassService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<InstructorClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<InstructorClassScheduleService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<InstructorMenuService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<LearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<LearningPathwayService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<LikeBySeenByService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<NotificationInboxService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ProfileService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<SchoolRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentAllClassService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassAssignmentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassAttendanceService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassScheduleService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentExamScheduleService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentMenuService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<TimelineService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ContactService> { RetrofitBuilder(get(), get()).build("https://sqlvaahtki34pd6xlc.blob.core.windows.net/") }
}