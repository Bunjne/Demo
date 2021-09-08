package whiz.sspark.library.data.dataSource.remote.service

import retrofit2.Response
import retrofit2.http.GET
import whiz.sspark.library.data.entity.ApiResponseX

interface ContactService {
    @GET("https://sqlvaahtki34pd6xlc.blob.core.windows.net/directory/directory.json?st=2021-02-08T07%3A08%3A15Z&se=2022-12-31T07%3A08%3A00Z&sp=" +
            "rl&sv=2018-03-28&sr=b&sig=cvaEV%2FPtbOjfOENpOHW2EdAcygH5rJCFWrMc%2Bzso99w%3D")
    suspend fun getContacts(): Response<ApiResponseX>
}