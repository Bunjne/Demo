package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.LearningPathwayService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface LearningPathwayRepository {
    suspend fun getLearningPathway(): Flow<DataWrapperX<List<LearningPathwayDTO>>>
    suspend fun addCourse(termId: String, courseId: String): Flow<DataWrapperX<String>>
    suspend fun deleteCourse(termId: String, courseId: String): Flow<DataWrapperX<String>>
}

class LearningPathwayRepositoryImpl(private val context: Context,
                                    private val remote: LearningPathwayService): LearningPathwayRepository {
    override suspend fun getLearningPathway(): Flow<DataWrapperX<List<LearningPathwayDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val item = listOf(
                        LearningPathwayDTO(
                            term = Term(
                                id = "123456789",
                                room = null,
                                term = 2,
                                year = 2021,
                                academicGrade = 10
                            ),
                            minCredit = 0,
                            maxCredit = 99,
                            _course = listOf(
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "AC007",
                                    credit = 1,
                                    nameEn = "วาดเส้นสถาปัตย์",
                                    nameTh = "วาดเส้นสถาปัตย์"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SP264",
                                    credit = 1,
                                    nameEn = "ศิลปะและการประกอบอาหาร",
                                    nameTh = "ศิลปะและการประกอบอาหาร"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "BE006",
                                    credit = 3,
                                    nameEn = "หลักการตลาด",
                                    nameTh = "หลักการตลาด"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "BE009",
                                    credit = 2,
                                    nameEn = "จิตวิทยาทางธุรกิจ",
                                    nameTh = "จิตวิทยาทางธุรกิจ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                )
                            ),
                            _requiredCourses = listOf(
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "TSS109",
                                    credit = 2,
                                    nameEn = "วิทยาการคำนวณ 2",
                                    nameTh = "วิทยาการคำนวณ 2"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "TSS207",
                                    credit = 2,
                                    nameEn = "การประยุกต์ใช้และแก้ปัญหาทางคณิตศาสตร์",
                                    nameTh = "การประยุกต์ใช้และแก้ปัญหาทางคณิตศาสตร์"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "TSS208",
                                    credit = 2,
                                    nameEn = "รู้ทันการเงิน",
                                    nameTh = "รู้ทันการเงิน"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "TSS304",
                                    credit = 2,
                                    nameEn = "ปรัชญาและการเมือง",
                                    nameTh = "ปรัชญาและการเมือง"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "TSS305",
                                    credit = 2,
                                    nameEn = "เศรษฐศาสตร์ที่มีชีวิต",
                                    nameTh = "เศรษฐศาสตร์ที่มีชีวิต"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "TSS402",
                                    credit = 2,
                                    nameEn = "นวัตกรรมและเทคโนโลยีสุขภาพ",
                                    nameTh = "นวัตกรรมและเทคโนโลยีสุขภาพ"
                                )
                            )
                        )
                    )
                    emit(
                        DataWrapperX(
                            data = item,
                            error = ApiResponseX(),
                            dataSource = DataSource.NETWORK
                        )
                    )
//                    val response = remote.getLearningPathway()
//                    fetchX(response, Array<LearningPathwayDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addCourse(termId: String, courseId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.addCourse(LearningPathwayAddCourseAPIBody(termId, courseId))
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteCourse(termId: String, courseId: String): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.deleteCourse(termId, courseId)
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}