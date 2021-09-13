package whiz.tss.sspark.s_spark_android.utility

import android.content.Context
import android.content.Intent
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.sspark.library.data.entity.RefreshTokenAPIBody
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.data.dataSource.remote.service.LoginService
import whiz.tss.sspark.s_spark_android.data.static.ConstantValue
import whiz.tss.sspark.s_spark_android.presentation.main.MainActivity
import java.util.*

fun retrieveAuthenticationInformation(context: Context): AuthenticationInformation? {
    val sharedPreference = try {
        EncryptedSharedPreferences.create(
            ConstantValue.sharedPreferencesAuthenticationInformationKey,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        context.getSharedPreferences(ConstantValue.sharedPreferencesAuthenticationInformationKey, Context.MODE_PRIVATE)
    }

    val authenticationInformationJson = sharedPreference.getString(ConstantValue.sharedPreferencesAuthenticationInformationKey, "")
    return if (authenticationInformationJson.isNullOrBlank()) {
        null
    } else {
        authenticationInformationJson.toObject()
    }
}

fun saveAuthenticationInformation(context: Context, authenticationInformation: AuthenticationInformation) {
    val sharedPreference = try {
        EncryptedSharedPreferences.create(
            ConstantValue.sharedPreferencesAuthenticationInformationKey,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        context.getSharedPreferences(ConstantValue.sharedPreferencesAuthenticationInformationKey, Context.MODE_PRIVATE)
    }

    val authenticationInformationJson = authenticationInformation.toJson()
    with(sharedPreference.edit()) {
        putString(ConstantValue.sharedPreferencesAuthenticationInformationKey, authenticationInformationJson)
        apply()
    }
}

fun generateDeviceID() = UUID.randomUUID().toString()

fun saveDeviceID(context: Context, deviceID: String) {
    val editor = context.getSharedPreferences(ConstantValue.sharedPreferencesDeviceIdKey, Context.MODE_PRIVATE).edit()
    with(editor) {
        putString(ConstantValue.sharedPreferencesDeviceIdKey, deviceID)
        apply()
    }
}

fun retrieveDeviceID(context: Context): String {
    var deviceID = context.getSharedPreferences(ConstantValue.sharedPreferencesDeviceIdKey, Context.MODE_PRIVATE).getString(ConstantValue.sharedPreferencesDeviceIdKey, "")
    if (deviceID.isNullOrBlank()) {
        deviceID = generateDeviceID()
        saveDeviceID(context, deviceID)
    }

    return deviceID
}

fun saveUserID(context: Context, userId: String) {
    val editor = context.getSharedPreferences(ConstantValue.sharedPreferencesUserIdKey, Context.MODE_PRIVATE).edit()
    with(editor) {
        putString(ConstantValue.sharedPreferencesUserIdKey, userId)
        apply()
    }
}

fun retrieveUserID(context: Context): String {
    val userId = context.getSharedPreferences(ConstantValue.sharedPreferencesUserIdKey, Context.MODE_PRIVATE).getString(ConstantValue.sharedPreferencesUserIdKey, "")
    return userId ?: ""
}

fun logout(context: Context) {
    clearData(context)

    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun clearData(context: Context) {
    val sharedPreferenceKeys = listOf(
        ConstantValue.sharedPreferencesUserIdKey,
        ConstantValue.sharedPreferencesAuthenticationInformationKey)

    sharedPreferenceKeys.forEach { key ->
        val editor = context.getSharedPreferences(key, Context.MODE_PRIVATE).edit()
        with(editor) {
            clear()
            apply()
        }
    }
}

fun refreshToken(context: Context, onTokenRefreshed: () -> Unit) {
    val refreshToken = retrieveAuthenticationInformation(context)?.refreshToken

    if (!refreshToken.isNullOrBlank()) {
        val remote: LoginService by inject(LoginService::class.java)
        val uuid = retrieveDeviceID(context)
        val userId = retrieveUserID(context)

        runBlocking {
            remote.refreshToken(RefreshTokenAPIBody(userId, uuid, refreshToken)).body()?.let {
                val authenticationInformation = it.data.toObject<AuthenticationInformation>()

                authenticationInformation?.let { authenticationInformation ->
                    saveAuthenticationInformation(context, authenticationInformation)
                    onTokenRefreshed()
                } ?: {
                    logout(context)
                }
            }
        }
    } else {
        logout(context)
    }
}