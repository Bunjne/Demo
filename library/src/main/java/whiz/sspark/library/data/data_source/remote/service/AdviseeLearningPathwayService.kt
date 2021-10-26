package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.LearningPathwayCourseAPIBody

interface AdviseeLearningPathwayService: LearningPathwayService {
    @GET("api/v1/pathways/me") //TODO wait confirm path
    suspend fun getLearningPathway(@Query("studentId") studentId: String): Response<ApiResponseX>
}