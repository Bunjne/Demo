package whiz.tss.sspark.s_spark_android.unility

import android.content.Context
import android.content.Intent
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.presentation.main.MainActivity
import whiz.tss.sspark.s_spark_android.data.static.ConstantValue
import java.lang.Exception
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
    val sharedPreferences = EncryptedSharedPreferences.create(
        ConstantValue.sharedPreferencesAuthenticationInformationKey,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val authenticationInformationJson = authenticationInformation.toJson()
    with(sharedPreferences.edit()) {
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

fun saveUserID(context: Context, deviceID: String) {
    val editor = context.getSharedPreferences(ConstantValue.sharedPreferencesUserIdKey, Context.MODE_PRIVATE).edit()
    with(editor) {
        putString(ConstantValue.sharedPreferencesUserIdKey, deviceID)
        apply()
    }
}

fun retrieveUserID(context: Context): String {
    var deviceID = context.getSharedPreferences(ConstantValue.sharedPreferencesUserIdKey, Context.MODE_PRIVATE).getString(ConstantValue.sharedPreferencesDeviceIdKey, "")
    if (deviceID.isNullOrBlank()) {
        deviceID = generateDeviceID()
        saveDeviceID(context, deviceID)
    }

    return deviceID
}



fun logout(context: Context) {
    val intent = Intent(context, MainActivity::class.java) //TODO change to LoginActivity
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}