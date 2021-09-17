package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.AddCourseService
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.utility.NetworkManager

interface AddCourseRepository {
    suspend fun getConcentrateCourse(): Flow<DataWrapperX<List<ConcentrateCourseDTO>>>
}

class AddCourseRepositoryImpl(private val context: Context,
                              private val remote: AddCourseService): AddCourseRepository {
    override suspend fun getConcentrateCourse(): Flow<DataWrapperX<List<ConcentrateCourseDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val item = listOf(
                        ConcentrateCourseDTO(
                            nameEn = "nameEn",
                            nameTh = "nameTh",
                            _courses = listOf(
                                CourseDTO(
                                    id = "courseID-123123123123",
                                    code = "AC00777",
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
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
                                ),
                                CourseDTO(
                                    id = "courseID-111",
                                    code = "SH003",
                                    credit = 2,
                                    nameEn = "การเมืองการปกครองเปรียบเทียบ",
                                    nameTh = "การเมืองการปกครองเปรียบเทียบ"
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
//                    val response = remote.getConcentrateCourse()
//                    fetchX(response, Array<ConcentrateCourseDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}