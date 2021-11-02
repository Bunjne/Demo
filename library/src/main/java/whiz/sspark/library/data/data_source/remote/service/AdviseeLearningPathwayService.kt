package whiz.sspark.library.data.data_source.remote.service

import retrofit2.Response
import retrofit2.http.*
import whiz.sspark.library.data.entity.ApiResponseX

interface AdviseeLearningPathwayService: LearningPathwayService {
    @GET("api/v1/pathways/{studentId}")
    suspend fun getLearningPathway(@Path("studentId") studentId: String): Response<ApiResponseX>
}