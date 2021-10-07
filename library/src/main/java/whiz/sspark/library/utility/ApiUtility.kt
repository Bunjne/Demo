package whiz.sspark.library.utility

import kotlinx.coroutines.flow.FlowCollector
import retrofit2.Response
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.DataSource
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import java.lang.Exception
import java.util.*

inline fun <reified T> transformToDataWrapperX(response: Response<ApiResponseX>): DataWrapperX<T> {
    val data = if (response.code() in 200..204) {
        if (T::class.java == String::class.java) {
            (response.body()?.message ?: "") as T
        } else {
            response.body()?.data?.toObject<T>()
        }
    } else {
        null
    }

    val error = try {
        response.errorBody()?.string()?.toObject<ApiResponseX>() ?: ApiResponseX(statusCode = response.code())
    } catch (e: Exception) {
        ApiResponseX(statusCode = response.code())
    }

    if (error.statusCode == 0) {
        error.statusCode = response.code()
    }

    return DataWrapperX(
        data = data,
        error = error,
        dataSource = DataSource.NETWORK,
        latestDateTime = Date()
    )
}

inline fun <reified T> transformToDataWrapperX(response: Response<ApiResponseX>, classOf: Class<Array<T>>): DataWrapperX<MutableList<T>> {
    val data = response.body()?.data?.toObjects(classOf)
    val error = try {
        response.errorBody()?.string()?.toObject<ApiResponseX>() ?: ApiResponseX(statusCode = response.code())
    } catch (e: Exception) {
        ApiResponseX(statusCode = response.code())
    }

    if (error.statusCode == 0) {
        error.statusCode = response.code()
    }

    return DataWrapperX(
        data = data,
        error = error,
        dataSource = DataSource.NETWORK,
        latestDateTime = Date()
    )
}

suspend inline fun <reified T> FlowCollector<DataWrapperX<T>>.fetchX(response: Response<ApiResponseX>) {
    if (response.code() == 401) {
        SSparkLibrary.onSessionExpired()
    } else {
        val dataWrapperX = transformToDataWrapperX<T>(response)
        emit(dataWrapperX)
    }
}

suspend inline fun <reified T> FlowCollector<DataWrapperX<MutableList<T>>>.fetchX(response: Response<ApiResponseX>,  classOf: Class<Array<T>>) {
    if (response.code() == 401) {
        SSparkLibrary.onSessionExpired()
    } else {
        val dataWrapperX = transformToDataWrapperX(response, classOf)
        emit(dataWrapperX)
    }
}