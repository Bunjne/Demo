package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.DataSource
import java.util.*

data class DataWrapper<out T>(val data: T?,
                              val error: ApiErrorResponse?,
                              val statusCode: Int? = null,
                              val isCacheExisted: Boolean? = null,
                              val latestDateTime: Date? = null,
                              val isNetworkPreferred: Boolean? = true,
                              val dataSource: DataSource
)