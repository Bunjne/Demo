package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.data_source.remote.RetrofitBuilder
import whiz.sspark.library.data.data_source.remote.service.*

val remoteModule = module {
    factory<AbilityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ActivityRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<AddCourseService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ClassGroupService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<ClassMemberService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<ClassPostCommentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<CourseSyllabusService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<ExpectOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<HappeningsService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<InstructorClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<LearningOutcomeService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<LearningPathwayService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<MenuStudentService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<SchoolRecordService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<StudentClassActivityService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<StudentClassAttendanceService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrlV3) }
    factory<StudentClassScheduleService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
    factory<TimelineService> { RetrofitBuilder(get(), get()).build(SSparkLibrary.baseUrl) }
}