package whiz.tss.sspark.s_spark_android.data.datasSource.remote

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.*
import whiz.sspark.library.data.entity.RefreshTokenAPIBody
import whiz.tss.sspark.s_spark_android.data.datasSource.remote.service.LoginService
import whiz.tss.sspark.s_spark_android.unility.logout
import whiz.tss.sspark.s_spark_android.unility.retrieveAuthenticationInformation
import whiz.tss.sspark.s_spark_android.unility.retrieveDeviceID
import whiz.tss.sspark.s_spark_android.unility.retrieveUserID

class TokenAuthenticator(private val context: Context,
                         private val remote: LoginService) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val refreshToken = retrieveAuthenticationInformation(context)?.refreshToken ?: ""
        val uuid = retrieveDeviceID(context)
        val userID = retrieveUserID(context)

        var token = ""
        runBlocking {
            token = remote.refreshToken(RefreshTokenAPIBody(userID, uuid, refreshToken)).body()?.accessToken ?: ""
        }

        if (token.isBlank()) {
            logout(context)
        }

        return response.request.newBuilder()
            .header("Authenticator", "bearer $token")
            .build();
    }
}