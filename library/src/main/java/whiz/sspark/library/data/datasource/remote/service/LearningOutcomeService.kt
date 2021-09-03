package whiz.sspark.library.data.datasource.remote.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import whiz.sspark.library.data.entity.ApiResponseX
import whiz.sspark.library.data.entity.LearningOutcomeAPIBody

interface LearningOutcomeService {
    @POST("v1/learningOutcome")
    suspend fun getLearningOutcome(@Body learningOutcomeAPIBody: LearningOutcomeAPIBody): ResponseBody
}