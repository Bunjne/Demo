package whiz.tss.sspark.s_spark_android.data.dataSource.remote

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.*
import whiz.sspark.library.SSparkLibrary
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.service.LoginService
import whiz.tss.sspark.s_spark_android.data.enum.GrantType
import whiz.tss.sspark.s_spark_android.extension.getAuthorizationToken
import whiz.tss.sspark.s_spark_android.utility.*

class TokenAuthenticator(private val context: Context,
                         private val remote: LoginService) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = retrieveAuthenticationInformation(context)?.refreshToken ?: ""
        var token = ""

        runBlocking {
            remote.refreshToken(
                client_id = SSparkLibrary.clientId,
                client_secret = SSparkLibrary.clientSecret,
                refreshToken = refreshToken,
                grant_type = GrantType.REFRESH_TOKEN.type
            ).body()?.let { authenticationInformation ->
                saveAuthenticationInformation(context, authenticationInformation)
                token = authenticationInformation.getAuthorizationToken()
            }
        }

        return if (token.isBlank()) {
            null
        } else {
            response.request.newBuilder()
                .header("Authorization", token)
                .build();
        }
    }
}